using CommunityToolkit.Maui.Views;

namespace yoga_d_client.app.page.yoga_class_page;

public partial class YogaClassPage: ContentPage
{
    private YogaClassViewModel vm;
    public YogaClassPage()
    {
        InitializeComponent();
        vm = new YogaClassViewModel();
        BindingContext = vm;
    }


    protected override async void OnAppearing()
    {
        base.OnAppearing();
        this.vm.LoadDataAsync();
    }


     private void Searching(object sender, EventArgs e)
    {
        AdvanceSearchAlert advanceSearchDialog = new AdvanceSearchAlert();
        advanceSearchDialog.SetOnDialogCallback((dayOfWeek, timeOfCourse) =>
        {
            this.vm.SearchData(dayOfWeek, timeOfCourse);
        });
        this.ShowPopup(advanceSearchDialog);
    }
}