package bgu.spl.net;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Course {
    private int courseNum;
    private String courseName;
    private int [] kdamCoursesList;
    private int numOfMaxStudent;
    //check if need to change the type
    private Queue<String> listOfStudent;

    public Course(int courseNum,String courseName, int[] kdamCoursesList, int numOfMaxStudent ){
        this.courseName=courseName;
        this.courseNum=courseNum;
        this.kdamCoursesList=kdamCoursesList;
        this.numOfMaxStudent=numOfMaxStudent;
        listOfStudent= new ConcurrentLinkedQueue<>();

    }
    public boolean areAvailableSeats(){
        return numOfMaxStudent>listOfStudent.size();
    }

    //check is the student has all the kdam courses are needed for this course.
    public boolean isStudentCanReg(ConcurrentLinkedQueue<Short> studentsCourses){
        for(Integer courseNum:kdamCoursesList) {
            if (studentsCourses == null || !studentsCourses.contains(courseNum.shortValue()))
                return false;
        }
        return true;
    }
    //return course status.
    public String getCourseStatus(){
        String s1= "Course: ("+this.courseNum+") "+this.courseName;
        String s2= "Seats Available: "+(this.numOfMaxStudent-this.listOfStudent.size())+"/"+this.numOfMaxStudent;
        String [] studentsArray=new String [this.listOfStudent.size()];
        this.listOfStudent.toArray(studentsArray);
        Arrays.sort(studentsArray);
        String s3= "Students Registered: "+ Arrays.toString(studentsArray).replaceAll(", ",",");
        return s1+'\n'+s2+'\n'+s3;
    }

    //add student that registered this course.
    public void addStudent (String studentUsername){
        listOfStudent.add(studentUsername);
    }
    public void removeStudent(String studentUsername){
        listOfStudent.remove(studentUsername);
    }
    public int[] getKdamCoursesList(){return this.kdamCoursesList;}
}
