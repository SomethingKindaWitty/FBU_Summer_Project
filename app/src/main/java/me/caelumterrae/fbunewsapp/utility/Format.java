package me.caelumterrae.fbunewsapp.utility;

public class Format {
    // converts bias string to political affiliation number
    public static int biasToNum(String bias) {
        if (bias == null) return 50;
        switch (bias) {
            case "right":
                return 100;
            case "right-center":
                return 75;
            case "center":
                return 50;
            case "leftcenter":
                return 25;
            case "left":
                return 0;
            default:
                return 50;
        }
    }

    // Convert "https://www.cnbc.com/2018/3/2..." to "cnbc.com"
    static public String trimUrl (String url) {
        final String http = "http://";
        final String https = "https://";
        final String www = "www.";

        int httpIndex = url.indexOf(http) + http.length();
        int httpsIndex = url.indexOf(https) + https.length();
        int wwwIndex = url.indexOf(www) + www.length();
        int beginIndex = Math.max(Math.max(httpIndex, httpsIndex), wwwIndex);
        String trimmedUrl = url.substring(beginIndex);
        int endIndex = trimmedUrl.indexOf("/");
        return trimmedUrl.substring(0, endIndex);
    }
}
