using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using yoga_d_client.app.database;
using yoga_d_client.app.model;
using yoga_d_client.app.page.yoga_detail_page;

namespace yoga_d_client.app.page.yoga_class_page
{
    public class YogaClassViewModel : ObservableObject
    {
        private bool isLoading = false;
        private FirebaseService firebaseService = new FirebaseService();
        public bool IsLoading
        {
            get { return this.isLoading; }
            set { SetProperty(ref isLoading, value); }
        }
        private ObservableCollection<YogaClass> yogaClassList = new ObservableCollection<YogaClass>();
        public ObservableCollection<YogaClass> YogaClasseList
        {
            get { return yogaClassList; }
            set { SetProperty(ref yogaClassList, value); }
        }
        public RelayCommand<YogaClass> OnClickedYogaClassCommand { get; private set; }
        public RelayCommand OnRefreshingCommand { get; private set; }

        public YogaClassViewModel()
        {
            OnClickedYogaClassCommand = new RelayCommand<YogaClass>(OnItemClicked);
            OnRefreshingCommand = new RelayCommand(LoadDataAsync, CanExcute);
        }
        public bool CanExcute()
        {
            return !IsLoading;
        }

        public void SearchData(int dayOfWeek, string timeOfCourse)
        {
            List<YogaClass> searchYogaList = new List<YogaClass>();
            foreach (YogaClass item in yogaClassList)
            {
                if (item.dayOfWeek == dayOfWeek || item.timeOfCourse == timeOfCourse)
                {
                    searchYogaList.Add(item);
                }
            }

            this.yogaClassList.Clear();
            searchYogaList.ForEach(yogaClass =>
            {
                yogaClassList.Add(yogaClass);
            });
        }

        public async void LoadDataAsync()
        {
            IsLoading = true;
            yogaClassList.Clear();
            List<YogaClass> yogaClasses = await firebaseService.GetYogaClass();
            yogaClasses.ForEach(yogaClass =>
            {
                yogaClassList.Add(yogaClass);
            });
            IsLoading = false;
        }



        public async void OnItemClicked(YogaClass yogaClass)
        {

            YogaClassDetailPage yogaClassPage = new YogaClassDetailPage(yogaClass);
            await Application.Current.MainPage
                .Navigation
                .PushAsync(yogaClassPage, true);
        }
    }
}
