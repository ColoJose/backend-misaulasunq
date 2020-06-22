package com.misaulasunq.controller.advice;

public class ErrorCodeGenerator {

    //Obs: Tambien podria manejarse con un mapa.
    public static String getErrorCode(Exception exception){
        String errorCode;

        switch (exception.getClass().getSimpleName()){
            case "ClassroomNotFoundException":
                errorCode = "01";
                break;
            case "DegreeNotFoundException":
                errorCode = "02";
                break;
            case "InconsistentRowException":
                errorCode = "03";
                break;
            case "InvalidCellFormatException":
                errorCode = "04";
                break;
            case "InvalidDayException":
                errorCode = "05";
                break;
            case "InvalidFileExtensionException":
                errorCode = "06";
                break;
            case "InvalidSemesterException":
                errorCode = "07";
                break;
            case "NoDataHeaderException":
                errorCode = "08";
                break;
            case "NoSheetFoundException":
                errorCode = "09";
                break;
            case "SubjectNotFoundException":
                errorCode = "10";
                break;
            default:
                errorCode = "50";
                break;
        }

        return errorCode;
    }
}
