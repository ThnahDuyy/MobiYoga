using CommunityToolkit.Mvvm.ComponentModel;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using yoga_d_client.app.model;

namespace yoga_d_client.app.page.class_instance_page
{
    public class ClassInstanceViewModel : ObservableObject
    {
        private ObservableCollection<ClassInstance> classInstanceList = new ObservableCollection<ClassInstance>();
        public ObservableCollection<ClassInstance> ClasseInstanceList
        {
            get { return classInstanceList; }
            set { SetProperty(ref classInstanceList, value); }
        }
        private List<ClassInstance> classInstances;
        public ClassInstanceViewModel(List<ClassInstance> classInstances)
        {
            this.classInstances = classInstances;
        }

        public async void LoadClassInstance()
        {
            this.classInstances.ForEach(classInstance =>
            {
                classInstanceList.Add(classInstance);
            });

        }
    }
}
