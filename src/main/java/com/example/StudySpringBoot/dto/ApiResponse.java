package com.example.StudySpringBoot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ApiResponse {
    private Response response;

    @JsonProperty("response")
    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public static class Response {
        private Header header;
        private Body body;

        @JsonProperty("header")
        public Header getHeader() {
            return header;
        }

        public void setHeader(Header header) {
            this.header = header;
        }

        @JsonProperty("body")
        public Body getBody() {
            return body;
        }

        public void setBody(Body body) {
            this.body = body;
        }
        
        
    }

    public static class Header {
        private String resultCode;
        private String resultMsg;

        @JsonProperty("resultCode")
        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        @JsonProperty("resultMsg")
        public String getResultMsg() {
            return resultMsg;
        }

        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }
    }

    public static class Body {
        private String dataType;
        private Items items;

        @JsonProperty("dataType")
        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        @JsonProperty("items")
        public Items getItems() {
            return items;
        }

        public void setItems(Items items) {
            this.items = items;
        }
    }

    public static class Items {
        private List<ApiItem> item;

        @JsonProperty("item")
        public List<ApiItem> getItem() {
            return item;
        }

        public void setItem(List<ApiItem> item) {
            this.item = item;
        }
    }

    public static class ApiItem {
        private String baseDate;
        private String baseTime;
        private String category;
        private String obsrValue;

        @JsonProperty("baseDate")
        public String getBaseDate() {
            return baseDate;
        }

        public void setBaseDate(String baseDate) {
            this.baseDate = baseDate;
        }

        @JsonProperty("baseTime")
        public String getBaseTime() {
            return baseTime;
        }

        public void setBaseTime(String baseTime) {
            this.baseTime = baseTime;
        }

        @JsonProperty("category")
        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        @JsonProperty("obsrValue")
        public String getObsrValue() {
            return obsrValue;
        }

        public void setObsrValue(String obsrValue) {
            this.obsrValue = obsrValue;
        }
    }
}
