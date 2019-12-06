package be.technocite.kafkavelib.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Station {

    private Integer number;
    @JsonProperty("contract_name")
    private String contractName;
    private String name;
    private String address;
    private Position position;
    private Boolean banking;
    private Boolean bonus;
    @JsonProperty("bike_stands")
    private Integer bikeStands;
    @JsonProperty("available_bike_stands")
    private Integer availableBikeStands;
    @JsonProperty("available_bikes")
    private Integer availableBikes;
    private String status;
    @JsonProperty("last_update")
    private Double lastUpdate;

    protected Station(){}

    public Station(Integer number, Integer availableBikeStands) {
        this.number = number;
        this.availableBikeStands = availableBikeStands;
    }

    public Station(Integer number, String contractName, String name, String address, Position position, Boolean banking, Boolean bonus, Integer bikeStands, Integer availableBikeStands, Integer availableBikes, String status, Double lastUpdate) {
        this.number = number;
        this.contractName = contractName;
        this.name = name;
        this.address = address;
        this.position = position;
        this.banking = banking;
        this.bonus = bonus;
        this.bikeStands = bikeStands;
        this.availableBikeStands = availableBikeStands;
        this.availableBikes = availableBikes;
        this.status = status;
        this.lastUpdate = lastUpdate;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Boolean getBanking() {
        return banking;
    }

    public void setBanking(Boolean banking) {
        this.banking = banking;
    }

    public Boolean getBonus() {
        return bonus;
    }

    public void setBonus(Boolean bonus) {
        this.bonus = bonus;
    }

    public Integer getBikeStands() {
        return bikeStands;
    }

    public void setBikeStands(Integer bikeStands) {
        this.bikeStands = bikeStands;
    }

    public Integer getAvailableBikeStands() {
        return availableBikeStands;
    }

    public void setAvailableBikeStands(Integer availableBikeStands) {
        this.availableBikeStands = availableBikeStands;
    }

    public Integer getAvailableBikes() {
        return availableBikes;
    }

    public void setAvailableBikes(Integer availableBikes) {
        this.availableBikes = availableBikes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Double lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}
