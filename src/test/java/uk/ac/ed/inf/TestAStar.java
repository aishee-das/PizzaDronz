package uk.ac.ed.inf;



// Main function
public class TestAStar {

    public static void main(String[] args){
        String BASEURL = "https://ilp-rest.azurewebsites.net";
        String testDate = "2023-11-31";
        String[] arguments = {testDate,BASEURL};
        App.main(arguments);
    }
}