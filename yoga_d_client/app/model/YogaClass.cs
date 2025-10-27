namespace yoga_d_client.app.model
{
    public class YogaClass
    {
        public string yogaClassId { get; set; }
        public string yogaName { get; set; }
        public int dayOfWeek { get; set; }
        public string  DayOfWeekString
        {
            get { return dayOfWeekToString(); }
            }
        public string timeOfCourse { get; set; }
        public int capacity { get; set; }
        public int duration { get; set; }
        public double price { get; set; }
        public string typeOfClass { get; set; }
        public string description { get; set; }

        public List<ClassInstance> classInstanceList = new List<ClassInstance>();

        public string dayOfWeekToString()
        {
            switch (dayOfWeek)
            {
                case 1: return "Sunday";
                case 2: return "Monday";
                case 3: return "Tuesday";
                case 4: return "Wednesday";
                case 5: return "Thursday";
                case 6: return "Friday";
                case 7: return "Saturday";
                default: return "Invalid date";
            }
        }

        public string priceToString()
        {
            return price + " £";
        }
        public string durationToString()
        {
            return duration + " mins";
        }
    }
}
