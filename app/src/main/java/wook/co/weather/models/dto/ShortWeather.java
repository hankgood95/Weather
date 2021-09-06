package wook.co.weather.models.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ShortWeather implements Serializable {

    @Expose
    @SerializedName("response")
    private Response response;

    public static class Response implements Serializable{
        @Expose
        @SerializedName("body")
        private Body body;
        @Expose
        @SerializedName("header")
        private Header header;

        public Body getBody() {
            return body;
        }

        public Header getHeader() {
            return header;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "body=" + body +
                    ", header=" + header +
                    '}';
        }
    }

    public static class Body implements Serializable{
        @Expose
        @SerializedName("totalCount")
        private int totalcount;
        @Expose
        @SerializedName("numOfRows")
        private int numofrows;
        @Expose
        @SerializedName("pageNo")
        private int pageno;
        @Expose
        @SerializedName("items")
        private Items items;
        @Expose
        @SerializedName("dataType")
        private String datatype;

        public int getTotalcount() {
            return totalcount;
        }

        public int getNumofrows() {
            return numofrows;
        }

        public int getPageno() {
            return pageno;
        }

        public Items getItems() {
            return items;
        }

        public String getDatatype() {
            return datatype;
        }

        @Override
        public String toString() {
            return "Body{" +
                    "totalcount=" + totalcount +
                    ", numofrows=" + numofrows +
                    ", pageno=" + pageno +
                    ", items=" + items +
                    ", datatype='" + datatype + '\'' +
                    '}';
        }
    }

    public static class Items implements Serializable{
        @Expose
        @SerializedName("item")
        private List<Item> item;

        public List<Item> getItem() {
            return item;
        }

        @Override
        public String toString() {
            return "Items{" +
                    "item=" + item +
                    '}';
        }
    }

    public static class Item implements Serializable{
        @Expose
        @SerializedName("ny")
        private int ny;
        @Expose
        @SerializedName("nx")
        private int nx;
        @Expose
        @SerializedName("fcstValue")
        private String fcstvalue;
        @Expose
        @SerializedName("fcstTime")
        private String fcsttime;
        @Expose
        @SerializedName("fcstDate")
        private String fcstdate;
        @Expose
        @SerializedName("category")
        private String category;
        @Expose
        @SerializedName("baseTime")
        private String basetime;
        @Expose
        @SerializedName("baseDate")
        private String basedate;

        public int getNy() {
            return ny;
        }

        public int getNx() {
            return nx;
        }

        public String getFcstvalue() {
            return fcstvalue;
        }

        public String getFcsttime() {
            return fcsttime;
        }

        public String getFcstdate() {
            return fcstdate;
        }

        public String getCategory() {
            return category;
        }

        public String getBasetime() {
            return basetime;
        }

        public String getBasedate() {
            return basedate;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "ny=" + ny +
                    ", nx=" + nx +
                    ", fcstvalue='" + fcstvalue + '\'' +
                    ", fcsttime='" + fcsttime + '\'' +
                    ", fcstdate='" + fcstdate + '\'' +
                    ", category='" + category + '\'' +
                    ", basetime='" + basetime + '\'' +
                    ", basedate='" + basedate + '\'' +
                    '}';
        }
    }

    public static class Header implements Serializable{
        @Expose
        @SerializedName("resultMsg")
        private String resultmsg;
        @Expose
        @SerializedName("resultCode")
        private String resultcode;

        public String getResultmsg() {
            return resultmsg;
        }

        public String getResultcode() {
            return resultcode;
        }

        @Override
        public String toString() {
            return "Header{" +
                    "resultmsg='" + resultmsg + '\'' +
                    ", resultcode='" + resultcode + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ShortWeather{" +
                "response=" + response +
                '}';
    }
}
