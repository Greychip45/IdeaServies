package com.example.ideaservices.Models;

public class servicesModel {
    private String serviceName,serviceImgUrl;

    public servicesModel(String serviceName, String serviceImgUrl) {
        this.serviceName = serviceName;
        this.serviceImgUrl = serviceImgUrl;
    }

    public servicesModel(String serviceName) {
        this.serviceName = serviceName;
    }

    public servicesModel(){

    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceImgUrl() {
        return serviceImgUrl;
    }

    public void setServiceImgUrl(String serviceImgUrl) {
        this.serviceImgUrl = serviceImgUrl;
    }


}
