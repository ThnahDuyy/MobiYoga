using Microsoft.Extensions.Logging;
using CommunityToolkit.Maui;
using yoga_d_client.app.page.yoga_class_page;

namespace yoga_d_client
{
    public static class MauiProgram
    {
        public static MauiApp CreateMauiApp()
        {
            var builder = MauiApp.CreateBuilder();
            builder.UseMauiApp<App>()
                .UseMauiCommunityToolkit()
                .Injection().ConfigureFonts(fonts =>
            {
                fonts.AddFont("OpenSans-Regular.ttf", "OpenSansRegular");
                fonts.AddFont("OpenSans-Semibold.ttf", "OpenSansSemibold");
            });
#if DEBUG
            builder.Logging.AddDebug();
#endif
            return builder.Build();
        }

        public static MauiAppBuilder Injection(this MauiAppBuilder mauiAppBuilder)
        {
            mauiAppBuilder.Services.AddTransient<YogaClassPage>();
            return mauiAppBuilder;
        }
    }
}