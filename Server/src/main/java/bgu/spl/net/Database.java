package bgu.spl.net;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {
	private static class DatabaseHolder {
		private static Database instance = new Database();
	}
	public final short ACK_OPCODE=12;
	public final short ERR_OPCODE=13;
	//Hashmap of all the courses , the key is the course number.
	private ConcurrentHashMap<Short,Course> coursesList;
	//Hashmap of all the admins that registered.
	private ConcurrentHashMap<String, String> registeredAdmins;
	//Hashmap of all the students that registered.
	private ConcurrentHashMap<String, String> registeredStudents;
	//Hashmap that contains the login status of the users
	private ConcurrentHashMap<String, Boolean> usersLoginStat;
	//Hashmap of all the students and the courses that they registered to.
	private ConcurrentHashMap<String, ConcurrentLinkedQueue<Short>> studentsCourses;
	// list of all the courses that sorted according to the "Courses.txt"
	private List<Short> sortedCoursesList;
	Object lockerLogin=new Object();
	Object lockerUserReg=new Object();
	Object lockerCourseReg=new Object();

	//to prevent user from creating new Database
	private Database() {
		this.coursesList = new ConcurrentHashMap<>();
		this.registeredStudents=new ConcurrentHashMap<>();
		this.registeredAdmins=new ConcurrentHashMap<>();
		this.studentsCourses=new ConcurrentHashMap<>();
		this.sortedCoursesList=new ArrayList<>();
		this.usersLoginStat=new ConcurrentHashMap<>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Database getInstance() {
		return DatabaseHolder.instance;
	}

	/**
	 * loades the courses from the file path specified
	 * into the Database, returns true if successful.
	 */
	public boolean initialize(String coursesFilePath) {
		boolean isInitialized = true;
		File file = new File(coursesFilePath);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String st;
			while ((st = br.readLine()) != null) {
				String[] lineData = st.split("\\|");
				Integer courseNum = Integer.parseInt(lineData[0]);
				String courseName = lineData[1];
				int[] kdamCoursesList;
				if (lineData[2].length() == 2)
					kdamCoursesList = new int[0];
				else {
					kdamCoursesList = Stream.of(lineData[2].substring(1, lineData[2].length() - 1).split(",")).mapToInt(Integer::parseInt).toArray();
				}
				int numOfMaxStudent = Integer.parseInt(lineData[3]);
				Course currCourse = new Course(courseNum, courseName, kdamCoursesList, numOfMaxStudent);
				coursesList.put(courseNum.shortValue(),currCourse);
				sortedCoursesList.add(courseNum.shortValue());
			}
		} catch (FileNotFoundException e) {
			isInitialized = false;
		} catch (IOException e) {
			isInitialized = false;
			e.printStackTrace();
		}
		return isInitialized;
	}
	// add  a new admin to the registeredAdmin hashmap if he does not exist
	public boolean addAdmin(String username, String password){
		synchronized (lockerUserReg) {
			//check if the user is already exist in the system
			if (registeredAdmins.containsKey(username) | registeredStudents.containsKey(username))
				return false;
			registeredAdmins.put(username, password);
			usersLoginStat.put(username, false);
			return true;
		}

	}
	// add a new student to the registeredStudents hashmap if he does not exist
	public boolean addStudent(String username, String password) {
		synchronized (lockerUserReg) {
			//check if the user is already exist in the system
			if (registeredAdmins.containsKey(username) | registeredStudents.containsKey(username))
				return false;
			registeredStudents.put(username, password);
			usersLoginStat.put(username, false);
			return true;
		}
	}


	//Try to register the student to the course which its number is 'courseNumber' . If possible return "true", otherwise "false".
	public boolean regCourse(String username, short courseNumber){
		synchronized (lockerCourseReg) {
			if (!coursesList.containsKey(courseNumber) || !coursesList.get(courseNumber).areAvailableSeats() ||
					!coursesList.get(courseNumber).isStudentCanReg(studentsCourses.get(username)))
				return false;
			//add the course to the student courses
			if (!studentsCourses.containsKey(username))
				studentsCourses.put(username, new ConcurrentLinkedQueue<>());
			studentsCourses.get(username).add(courseNumber);
			coursesList.get(courseNumber).addStudent(username);
			return true;
		}
	}
	//return String that represent list of kdam courses.
	public String kdamCourseCheck (String username, boolean isLogin, short courseNumber, String userType){
		if(!isLogin||userType.equals("Admin")|| !coursesList.containsKey(courseNumber))
			return null;
		return Arrays.toString(coursesList.get(courseNumber).getKdamCoursesList()).replaceAll(", ",",");
	}
	//return String that represent list of course status.
	public String courseStatusCheck(String username, boolean isLogin, short courseNumber,String userType){
		if(!isLogin|| userType.equals("Student")||!this.coursesList.containsKey(courseNumber))
			return null;
		return this.coursesList.get(courseNumber).getCourseStatus();
	}
	// return String that represent the student status
	public String studentStatusCheck (String username, boolean isLogin, String studentUsername, String userType) {

		if (!isLogin|| userType.equals("Student")||!registeredStudents.containsKey(studentUsername) ){

			return null;
		}
		String s1 = "Student: " + studentUsername;
		ConcurrentLinkedQueue<Short> studentUsernameCourses = studentsCourses.get(studentUsername);
		Short[] sortedCourses;
		if(studentUsernameCourses!=null)
			sortedCourses = new Short[studentUsernameCourses.size()];
		else
			sortedCourses=new Short [0];
		sortCoursesByTxt(sortedCourses,studentUsernameCourses);
		String s2 = "Courses: " + Arrays.toString(sortedCourses).replaceAll(", ",",");
		return s1 + '\n' + s2;
	}
	private void sortCoursesByTxt(Short[] sortedCourses,ConcurrentLinkedQueue<Short> studentCourses){
		int index = 0;
		if (sortedCourses.length > 0) {
			for (Short course : sortedCoursesList) {
				if (studentCourses.contains(course)) {
					sortedCourses[index] = course;
					index++;
				}
			}
		}
	}

	//check if the student is registered to the course.
	public String isRegisteredCourse (String username, boolean isLogin, short courseNumber, String userType) {
		if (!isLogin || userType.equals("Admin") || !this.coursesList.containsKey(courseNumber))
			return null;
		if (studentsCourses.get(username)!=null &&studentsCourses.get(username).contains(courseNumber))
			return "REGISTERED";
		else
			return "NOT REGISTERED";
	}
	//unregistered the student from the course.
	public boolean unregisterCourse(String username, short courseNumber) {
		if (!this.coursesList.containsKey(courseNumber) || studentsCourses.get(username)==null || !studentsCourses.get(username).contains(courseNumber))
			return false;
		this.coursesList.get(courseNumber).removeStudent(username);
		this.studentsCourses.get(username).remove(courseNumber);
		return true;
	}

	public String getStudentCourses(String username, boolean isLogin, String userType){
		if(!isLogin || userType.equals("Admin"))
			return null;
		int size=0;
		if(studentsCourses.get(username)!=null)
			size=studentsCourses.get(username).size();
		Short [] sortedCourses=new Short[size];
		sortCoursesByTxt(sortedCourses,studentsCourses.get(username));
		//this.studentsCourses.get(username).toArray(sortedCourses);
		return Arrays.toString(sortedCourses).replaceAll(", ",",");
	}
	public String getUserType(String username){
		if(registeredAdmins.containsKey(username))
			return "Admin";
		else if(registeredStudents.containsKey(username))
			return "Student";
		return null;
	}
	public boolean checkLogin (String username, String password) {
		synchronized (lockerLogin) {
			String userType = getUserType(username);
			if (userType == null || usersLoginStat.get(username) ||
					(userType.equals("Student") && !registeredStudents.get(username).equals(password))
					|| (userType.equals("Admin") && !registeredAdmins.get(username).equals(password)))
				return false;
			usersLoginStat.put(username, true);
			return true;
		}
	}
	public void logout (String username){
		usersLoginStat.put(username, false);
	}
}
