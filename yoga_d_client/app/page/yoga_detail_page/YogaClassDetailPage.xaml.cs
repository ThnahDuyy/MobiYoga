using yoga_d_client.app.model;
using yoga_d_client.app.page.class_instance_page;

namespace yoga_d_client.app.page.yoga_detail_page;

public partial class YogaClassDetailPage : ContentPage
{
    private YogaClass yogaClass;

    public YogaClassDetailPage(YogaClass yogaClass)
    {
        InitializeComponent();
        this.yogaClass = yogaClass;

    }

    private void backClicked(object sender, EventArgs e)
    {
        Navigation.PopAsync();
    }

    protected override async void OnAppearing()
    {
        base.OnAppearing();
       
        name.Text = "Name: " + yogaClass.yogaName.ToString();
        price.Text = "Price: " + yogaClass.priceToString();
        capacity.Text = "Capacity: " + yogaClass.capacity.ToString();
        day_of_week.Text = "Day of week: " + yogaClass.dayOfWeekToString();
        type_of_class.Text = "Type of class: " + yogaClass.typeOfClass;
        time_of_course.Text = "Time of course: " + yogaClass.timeOfCourse;
        duration.Text = "Duration: " + yogaClass.durationToString();
        description.Text = "description: " + yogaClass.description;
    }

    private async void ClassInstanceClicked(object sender, EventArgs e)
    {
        ClassInstanceViewModel classInstanceViewModel = new ClassInstanceViewModel(yogaClass.classInstanceList);
        ClassInstancePage classIsntanceView = new ClassInstancePage(classInstanceViewModel);
        await Application.Current.MainPage
            .Navigation
            .PushAsync(classIsntanceView, true);
    }
}