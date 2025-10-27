namespace yoga_d_client.app.page.class_instance_page;

public partial class ClassInstancePage : ContentPage
{
    private ClassInstanceViewModel vm;

    public ClassInstancePage(ClassInstanceViewModel classInstanceViewModel)
    {
        InitializeComponent();
        this.vm = classInstanceViewModel;
        BindingContext = vm;
    }

    protected override async void OnAppearing()
    {
        base.OnAppearing();
        this.vm.LoadClassInstance();
    }

}