package ca.ucalgary.rules599.model;


import ca.ucalgary.rules599.datastructure.TransactionalItemSet;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccidentAttributeHolder {

    private List<String> C_YEAR; //Year	19yy-20yy. yy = last two digits of the calendar year. (e.g. 90, 91, 92)
    private List<String> C_MNTH; //Month	01-12. UU=Unknown, XX Jurisdiction does not provide the data
    private List<String> C_WDAY; //Day of week	1-7.  U=Unknown, X Jurisdiction does not provide the data
    private List<String> C_HOUR; // Collision hour	00 - 23. UU=Unknown, XX Jurisdiction does not provide the data
    private List<String> C_SEV; //Collision severity	1-2. U=Unknown, X Jurisdiction does not provide the data
    private List<String> C_VEHS; // Number of vehicles involved in Collision	01-98. 99= 99 or More vehicles involved. UU=Unknown, XX Jurisdiction does not provide the data
    private List<String> C_CONF; // Collision configuration
    private List<String> C_RCFG; //Roadway configuration	01-12. QQ= Choice is other than the preceding values. UU=Unknown, XX Jurisdiction does not provide the data
    private List<String> C_WTHR; // Weather condition	1-7. Q= Choice is other than the preceding values. U=Unknown, X= Jurisdiction does not provide the data
    private List<String> C_RSUR; //Road surface	1-9. Q= Choice is other than the preceding values. U=Unknown, X= Jurisdiction does not provide the data
    private List<String> C_RALN; //Road alignment	1-6. Q= Choice is other than the preceding values. U=Unknown, X= Jurisdiction does not provide the data
    private List<String> C_TRAF; // Traffic control	01-18. QQ= Choice is other than the preceding values. UU=Unknown, XX=Jurisdiction does not provide the data

    private List<String> P_SEX; //Person sex	F=Female, M=Male, N= Data Element not applicable, U=Unknown, X=Jurisdiction does not provide this data
    private List<String> P_AGE; //Person age
    private List<String> P_PSN;  //Person position
    private List<String> P_ISEV; //Medical treatment required
    private List<String> P_SAFE; //Safety device used
    private List<String> P_USER; //Road user class

    private List<String> V_TYPE; //Vehicle type	01-23. NN=Data not Applicable. QQ= Choice is other than the preceding values. UU=Unknown, XX=Jurisdiction does not provide the data
    private List<String> V_YEAR; //Vehicle model year	19yy-20yy. NNNN= Data Element not Applicable. UUUU=Unknown, XXXX=Jurisdiction does not provide the data



    public void  updateFields(String fieldName, List<String> values){
        switch (fieldName.toUpperCase()){
            case "C_YEAR":
                setC_YEAR(values);
                break;
            case "C_MNTH":
                setC_MNTH(values);
                break;
            case "C_WDAY":
                setC_WDAY(values);
                break;
            case "C_HOUR":
                setC_HOUR(values);
                break;
            case "C_SEV":
                setC_SEV(values);
                break;
            case "C_VEHS":
                setC_VEHS(values);
                break;
            case "C_CONF":
                setC_CONF(values);
                break;
            case "C_RCFG":
                setC_RCFG(values);
                break;
            case "C_WTHR":
                setC_WTHR(values);
                break;
            case "C_RSUR":
                setC_RSUR(values);
                break;
            case "C_RALN":
                setC_RALN(values);
                break;
            case "C_TRAF":
                setC_TRAF(values);
                break;

                case "P_SEX":
                setP_SEX(values);
                break;
            case "P_AGE":
                setP_AGE(values);
                break;
            case "P_PSN":
                setP_PSN(values);
                break;
            case "P_ISEV":
                setP_ISEV(values);
                break;
            case "P_SAFE":
                setP_SAFE(values);
                break;
            case "P_USER":
                setP_USER(values);
                break;


            case "V_TYPE":
                setV_TYPE(values);
                break;
            case "V_YEAR":
                setV_YEAR(values);
                break;
                default:
        }

    }


    public List<Map<String, int[]>>  createList(Map<Integer, TransactionalItemSet<AccidentAttribute>> frequentItemSets) {
        List<Map<String, int[]>> geneticList = new ArrayList<>();
        for (Map.Entry<Integer, TransactionalItemSet<AccidentAttribute>> itemSet : frequentItemSets.entrySet()) {
            Map<String, int[]> holderList = getInitialIntegerMap();
            for (AccidentAttribute setString : itemSet.getValue()) {
                String[] splitString = setString.getName().replace(")", "").split("[(]");
                String attributeName = splitString[0];
                String attributeValue = splitString[1];
                int[] intArray = null;
                switch (attributeName.toUpperCase()) {
                    case "C_YEAR":
                        intArray = new int[getC_YEAR().size()];
                        intArray[getC_YEAR().indexOf(attributeValue)] = 1;
                        holderList.replace(attributeName.toUpperCase(), intArray);
                        break;
                    case "C_MNTH":
                        intArray = new int[getC_MNTH().size()];
                        intArray[getC_MNTH().indexOf(attributeValue)] = 1;
                        holderList.replace(attributeName.toUpperCase(), intArray);
                        break;
                    case "C_WDAY":
                        intArray = new int[getC_WDAY().size()];
                        intArray[getC_WDAY().indexOf(attributeValue)] = 1;
                        holderList.replace(attributeName.toUpperCase(), intArray);
                        break;
                    case "C_HOUR":
                        intArray = new int[getC_HOUR().size()];
                        intArray[getC_HOUR().indexOf(attributeValue)] = 1;
                        holderList.replace(attributeName.toUpperCase(), intArray);
                        break;
                    case "C_SEV":
                        intArray = new int[getC_SEV().size()];
                        intArray[getC_SEV().indexOf(attributeValue)] = 1;
                        holderList.replace(attributeName.toUpperCase(), intArray);
                        break;
                    case "C_VEHS":
                        intArray = new int[getC_VEHS().size()];
                        intArray[getC_VEHS().indexOf(isContained(getC_VEHS(), Integer.parseInt(attributeValue)))] = 1;
                        holderList.replace(attributeName.toUpperCase(), intArray);
                        break;
                    case "C_CONF":
                        intArray = new int[getC_CONF().size()];
                        intArray[getC_CONF().indexOf(attributeValue)] = 1;
                        holderList.replace(attributeName.toUpperCase(), intArray);
                        break;
                    case "C_RCFG":
                        intArray = new int[getC_RCFG().size()];
                        intArray[getC_RCFG().indexOf(attributeValue)] = 1;
                        holderList.replace(attributeName.toUpperCase(), intArray);
                        break;
                    case "C_WTHR":
                        intArray = new int[getC_WTHR().size()];
                        intArray[getC_WTHR().indexOf(attributeValue)] = 1;
                        holderList.replace(attributeName.toUpperCase(), intArray);
                        break;
                    case "C_RSUR":
                        intArray = new int[getC_RSUR().size()];
                        intArray[getC_RSUR().indexOf(attributeValue)] = 1;
                        holderList.replace(attributeName.toUpperCase(), intArray);
                        break;
                    case "C_RALN":
                        intArray = new int[getC_RALN().size()];
                        intArray[getC_RALN().indexOf(attributeValue)] = 1;
                        holderList.replace(attributeName.toUpperCase(), intArray);
                        break;
                    case "C_TRAF":
                        intArray = new int[getC_TRAF().size()];
                        intArray[getC_TRAF().indexOf(attributeValue)] = 1;
                        holderList.replace(attributeName.toUpperCase(), intArray);
                        break;

                    case "P_SEX":
                        intArray = new int[getP_SEX().size()];
                        intArray[getP_SEX().indexOf(attributeValue)] = 1;
                        holderList.replace(attributeName.toUpperCase(), intArray);
                        break;
                    case "P_AGE":
                        intArray = new int[getP_AGE().size()];
                        intArray[getP_AGE().indexOf(isContained(getP_AGE(), Integer.parseInt(attributeValue)))] = 1;
                        holderList.replace(attributeName.toUpperCase(), intArray);
                        break;
                    case "P_PSN":
                        intArray = new int[getP_PSN().size()];
                        intArray[getP_PSN().indexOf(attributeValue)] = 1;
                        holderList.replace(attributeName.toUpperCase(), intArray);
                        break;
                    case "P_ISEV":
                        intArray = new int[getP_ISEV().size()];
                        intArray[getP_ISEV().indexOf(attributeValue)] = 1;
                        holderList.replace(attributeName.toUpperCase(), intArray);
                        break;
                    case "P_SAFE":
                        intArray = new int[getP_SAFE().size()];
                        intArray[getP_SAFE().indexOf(attributeValue)] = 1;
                        holderList.replace(attributeName.toUpperCase(), intArray);
                        break;
                    case "P_USER":
                        intArray = new int[getP_USER().size()];
                        intArray[getP_USER().indexOf(attributeValue)] = 1;
                        holderList.replace(attributeName.toUpperCase(), intArray);
                        break;


                    case "V_TYPE":
                        intArray = new int[getV_TYPE().size()];
                        intArray[getV_TYPE().indexOf(attributeValue)] = 1;
                        holderList.replace(attributeName.toUpperCase(), intArray);
                        break;
                    case "V_YEAR":
                        intArray = new int[getV_YEAR().size()];
                        intArray[getV_YEAR().indexOf(attributeValue)] = 1;
                        holderList.replace(attributeName.toUpperCase(), intArray);
                        break;
                    default:
                }

            }
            geneticList.add(holderList);
        }
        return geneticList;
    }

    private Map<String, int[]> getInitialIntegerMap(){
        Map<String, int[]> holderList = new HashMap<>();
        holderList.put("C_YEAR", new int[getC_YEAR().size()]);
        holderList.put("C_MNTH", new int[getC_MNTH().size()]);
        holderList.put("C_WDAY", new int[getC_WDAY().size()]);
        holderList.put("C_HOUR", new int[getC_HOUR().size()]);
        holderList.put("C_SEV", new int[getC_SEV().size()]);
        holderList.put("C_VEHS", new int[getC_VEHS().size()]);
        holderList.put("C_RCFG", new int[getC_RCFG().size()]);
        holderList.put("C_WTHR", new int[getC_WTHR().size()]);
        holderList.put("C_RSUR", new int[getC_RSUR().size()]);
        holderList.put("C_RALN", new int[getC_RALN().size()]);
        holderList.put("C_TRAF", new int[getC_TRAF().size()]);
        holderList.put("P_SEX", new int[getP_SEX().size()]);
        holderList.put("P_AGE", new int[getP_AGE().size()]);
        holderList.put("P_PSN", new int[getP_PSN().size()]);
        holderList.put("P_ISEV", new int[getP_ISEV().size()]);
        holderList.put("P_SAFE", new int[getP_SAFE().size()]);
        holderList.put("P_USER", new int[getP_USER().size()]);

        return holderList;
    }


    public int getSize(){

        return getC_CONF().size() + getC_HOUR().size()+ getC_MNTH().size() + getC_RALN().size()+ getC_RCFG().size()+ getC_RSUR().size()+ getC_SEV().size()
                +getC_TRAF().size()+getC_VEHS().size()+getC_WDAY().size()+getC_WTHR().size()+ getC_YEAR().size()+getP_AGE().size()+getP_ISEV().size()
                +getP_PSN().size()+getP_SAFE().size()+getP_SEX().size()+getP_USER().size()+getV_TYPE().size()+getV_YEAR().size();


    }

    private String isContained(List<String> list, int value){
        String returnVal=null;
        for(String string:list){
          String[] strings=string.split("-");
          int low=Integer.parseInt(strings[0]);
          int high=Integer.parseInt(strings[1]);
          if ((low >=value && high<=value) || (low <=value && high>=value)){
              returnVal=string;
              break;
          }
        }
        return returnVal;
    }

}
