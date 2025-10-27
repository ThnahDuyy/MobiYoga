using CommunityToolkit.Maui.Views;


namespace yoga_d_client.app.page.yoga_class_page;

public partial class AdvanceSearchAlert : Popup
{
    public delegate void AlertResultEvent(int dayOfWeek, string timeOfCourse);
    public AlertResultEvent alertEvent { get; set; }
    private List<string> dayOfWeekArr;
    public AdvanceSearchAlert()
    {
        InitializeComponent();
        dayOfWeekArr = new List<string>() { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };

        date_of_week.ItemsSource = dayOfWeekArr;
        time_of_course.Text = "";
    }

    public void SetOnDialogCallback(AlertResultEvent alertEvent)
    {
        this.alertEvent = alertEvent;
    }

    private void OnSearch(object sender, EventArgs e)
    {
        int dayOfWeek = -1;
        if (date_of_week.SelectedIndex != -1)
        {
            dayOfWeek = date_of_week.SelectedIndex + 1;
        }
        
        this.alertEvent(dayOfWeek, time_of_course.Text);
        this.Close();
    }
}