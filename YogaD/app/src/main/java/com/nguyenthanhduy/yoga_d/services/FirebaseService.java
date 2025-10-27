package com.nguyenthanhduy.yoga_d.services;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.nguyenthanhduy.yoga_d.database.ClassInstanceDB;
import com.nguyenthanhduy.yoga_d.database.YogaClassDB;
import com.nguyenthanhduy.yoga_d.model.ClassInstance;
import com.nguyenthanhduy.yoga_d.model.YogaClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FirebaseService {
    private final FirebaseServiceEvent event;
    List<YogaClass> yogaClasses;
    List<ClassInstance> classInstances;

    public interface FirebaseServiceEvent {

        public void success();

        public void fail(String error);
    }

    public FirebaseService(List<YogaClass> yogaClasses, List<ClassInstance> classInstances, FirebaseServiceEvent event) {
        this.yogaClasses = yogaClasses;
        this.classInstances = classInstances;
        this.event = event;
    }

    private CompletableFuture<Boolean> syncYogaThread(List<Map<String, Object>> yogaClassHashList) {
        CompletableFuture<Boolean> thread = new CompletableFuture<>();
        FirebaseFirestore firebaseInstance = FirebaseFirestore.getInstance();
        firebaseInstance.collection(YogaClass.class.getSimpleName())
                .get().addOnCompleteListener(taskQuery -> {
                    WriteBatch batchYogaClass = firebaseInstance.batch();
                    for (QueryDocumentSnapshot document : taskQuery.getResult()) {
                        batchYogaClass.delete(document.getReference());
                    }
                    for (Map<String, Object> yogaClassHash : yogaClassHashList) {
                        DocumentReference docRef = firebaseInstance.collection(YogaClass.class.getSimpleName()).document();
                        batchYogaClass.set(docRef, yogaClassHash);
                    }
                    batchYogaClass.commit()
                            .addOnSuccessListener(aVoid -> {
                                thread.complete(true);
                            })
                            .addOnFailureListener(e -> {
                                thread.complete(false);
                            });
                });

        return thread;
    }

    private CompletableFuture<Boolean> syncClassInstanceThread(List<Map<String, Object>> classInstanceHashList) {
        CompletableFuture<Boolean> thread = new CompletableFuture<>();
        FirebaseFirestore firebaseInstance = FirebaseFirestore.getInstance();
        firebaseInstance.collection(ClassInstance.class.getSimpleName()).get().addOnCompleteListener(taskQuery -> {
            WriteBatch batch_class_instance = firebaseInstance.batch();
            for (QueryDocumentSnapshot document : taskQuery.getResult()) {
                batch_class_instance.delete(document.getReference());
            }
            for (Map<String, Object> classInstanceHash : classInstanceHashList) {
                DocumentReference docRef = firebaseInstance.collection(ClassInstance.class.getSimpleName()).document();
                batch_class_instance.set(docRef, classInstanceHash);
            }
            batch_class_instance.commit()
                    .addOnSuccessListener(aVoid -> {
                        thread.complete(true);
                    })
                    .addOnFailureListener(e -> {
                        thread.complete(false);
                    });
        });
        return thread;
    }

    private List<Map<String, Object>> convertYogaClassToHashList() {
        List<Map<String, Object>> yogaClassHashList = new ArrayList<>();
        for (int i = 0; i < yogaClasses.size(); i++) {
            Map<String, Object> yogaClassHash = new HashMap<>();
            YogaClass yogaClass = yogaClasses.get(i);
            yogaClassHash.put(YogaClassDB.NAME, yogaClass.yogaName);
            yogaClassHash.put(YogaClassDB.YOGA_CLASS_ID, yogaClass.yogaClassID);
            yogaClassHash.put(YogaClassDB.PRICE, yogaClass.getPrice());
            yogaClassHash.put(YogaClassDB.CAPACITY, yogaClass.capacity);
            yogaClassHash.put(YogaClassDB.DAY_OF_WEEK, yogaClass.dayOfWeek);
            yogaClassHash.put(YogaClassDB.TYPE_OF_CLASS, yogaClass.typeOfClass);
            yogaClassHash.put(YogaClassDB.DESCRIPTION, yogaClass.description);
            yogaClassHash.put(YogaClassDB.TIME_OF_COURSE, yogaClass.timeOfCourse);
            yogaClassHash.put(YogaClassDB.DURATION, yogaClass.getDuration());
            yogaClassHashList.add(yogaClassHash);
        }
        return yogaClassHashList;
    }

    private List<Map<String, Object>> convertClassInstanceToHashList() {
        List<Map<String, Object>> classInstanceHashList = new ArrayList<>();
        for (int i = 0; i < classInstances.size(); i++) {
            Map<String, Object> classInstanceHash = new HashMap<>();
            ClassInstance classInstance = classInstances.get(i);
            classInstanceHash.put(ClassInstanceDB.CLASS_INSTANCE_ID, classInstance.classInstanceID);
            classInstanceHash.put(YogaClassDB.YOGA_CLASS_ID, classInstance.yogaClassID);
            classInstanceHash.put(ClassInstanceDB.COMMENT, classInstance.comment);
            classInstanceHash.put(ClassInstanceDB.DATE, classInstance.date);
            classInstanceHash.put(ClassInstanceDB.TEACHER, classInstance.teacher);
            classInstanceHashList.add(classInstanceHash);
        }
        return classInstanceHashList;
    }

    public void startSync() {
        CompletableFuture<Boolean> syncYogaClass = syncYogaThread(convertYogaClassToHashList());
        CompletableFuture<Boolean> syncClassInstance = syncClassInstanceThread(convertClassInstanceToHashList());
        CompletableFuture<Void> syncThreadAll = CompletableFuture.allOf(syncYogaClass, syncClassInstance);
        syncThreadAll.thenRun(() -> {
            try {
                Boolean responseYogaClass = syncYogaClass.get();
                Boolean responseClassInstance = syncClassInstance.get();

                if (responseYogaClass && responseClassInstance) {
                    event.success();
                } else {
                    event.fail("sync to firebase failed");
                }
            } catch (Exception e) {
                Log.e("FirebaseService", "startSync: " + e.getMessage());
                event.fail("sync to firebase failed");
            }
        });
    }
}
