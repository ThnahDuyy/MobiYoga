using Google.Cloud.Firestore.V1;
using Google.Cloud.Firestore;
using yoga_d_client.app.model;
using yoga_d_client.app.constants;

namespace yoga_d_client.app.database
{
    public class FirebaseService
    {
        public const string PROJECT_ID = "yogad-20e16";
        private FirestoreDb firestoreDB;
        private const string YOGA_CLASS_COLLECTION = "YogaClass";
        private const string CLASS_INSTANCE_COLLECTION = "ClassInstance";

        public FirebaseService()
        {
            InitService();
        }

        public async Task InitService()
        {
            var stream = await FileSystem.OpenAppPackageFileAsync("service_account_firebase.json");
            var reader = new StreamReader(stream);
            var contents = reader.ReadToEnd();

            FirestoreClientBuilder fbc = new FirestoreClientBuilder { JsonCredentials = contents };
            firestoreDB = FirestoreDb.Create(PROJECT_ID, fbc.Build());
        }

        public async Task<List<YogaClass>> GetYogaClass()
        {
            List<YogaClass> yogaClassList = new List<YogaClass>();


            CollectionReference yogaClassCollection = firestoreDB.Collection(YOGA_CLASS_COLLECTION);
            QuerySnapshot snapshot = await yogaClassCollection.GetSnapshotAsync();

            foreach (DocumentSnapshot document in snapshot.Documents)
            {
                if (document.Exists)
                {
                    Dictionary<string, object> documentDictionary = document.ToDictionary();
                    YogaClass yogaClass = new YogaClass();
                    yogaClass.yogaClassId = documentDictionary[YogaClassKey.YOGA_CLASS_ID].ToString();
                    yogaClass.yogaName = documentDictionary[YogaClassKey.YOGA_NAME].ToString();
                    yogaClass.capacity = int.Parse(documentDictionary[YogaClassKey.CAPACITY].ToString());
                    yogaClass.dayOfWeek = int.Parse(documentDictionary[YogaClassKey.DAY_OF_WEEK].ToString());
                    yogaClass.description = documentDictionary[YogaClassKey.DESCRIPTION].ToString();
                    yogaClass.duration = int.Parse(documentDictionary[YogaClassKey.DURATION].ToString());
                    yogaClass.price = double.Parse(documentDictionary[YogaClassKey.DURATION].ToString());
                    yogaClass.timeOfCourse = documentDictionary[YogaClassKey.TIME_OF_COURSE].ToString();
                    yogaClass.typeOfClass = documentDictionary[YogaClassKey.TYPE_OF_CLASS].ToString();


                    yogaClass.classInstanceList = await GetClassInstance(yogaClass.yogaClassId);
                    yogaClassList.Add(yogaClass);
                }
            }

            return yogaClassList;
        }

        public async Task<List<ClassInstance>> GetClassInstance(string yogaId)
        {
            List<ClassInstance> classInstanceList = new List<ClassInstance>();
            CollectionReference classIstanceCollection = firestoreDB.Collection(CLASS_INSTANCE_COLLECTION);
            QuerySnapshot snapshot = await classIstanceCollection.WhereEqualTo(ClassInstanceKey.YOGA_CLASS_ID, yogaId).GetSnapshotAsync();
            foreach (DocumentSnapshot document in snapshot.Documents)
            {
                if (document.Exists)
                {
                    Dictionary<string, object> documentDictionary = document.ToDictionary();
                    ClassInstance classInstance = new ClassInstance();
                    classInstance.classInstanceId = documentDictionary[ClassInstanceKey.CLASS_INSTANCE_ID].ToString();
                    classInstance.comment = documentDictionary[ClassInstanceKey.COMMENT].ToString();
                    classInstance.date = documentDictionary[ClassInstanceKey.DATE].ToString();
                    classInstance.teacher = documentDictionary[ClassInstanceKey.TEACHER].ToString();
                    classInstance.yogaClassId = documentDictionary[ClassInstanceKey.YOGA_CLASS_ID].ToString();
                    classInstanceList.Add(classInstance);
                }
            }
            return classInstanceList;
        }
    }
}
