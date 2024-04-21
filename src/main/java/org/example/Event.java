package org.example;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Event {
    static ArrayList<BasicData> Admin;
    static ArrayList<BasicData> Student;

    Event()
    {
        try{
            Admin = new ArrayList<BasicData>();
            Student = new ArrayList<BasicData>();

            BufferedReader in_Admin = new BufferedReader (new FileReader("admin.txt"));
            BufferedReader in_Student = new BufferedReader (new FileReader("student.txt"));

            String s;
            String id;
            String name;
            String password;

            while((s = in_Admin.readLine()) != null)
            {
                id = s.substring(3,s.indexOf("Name") - 2);
                name = s.substring(s.indexOf("Name") + 5, s.indexOf("Password") - 2);
                password = s.substring(s.indexOf("Password") + 9, s.length());

                BasicData obj = new BasicData(Integer.parseInt(id), name, password);
                Admin.add(obj);

            }

            while((s = in_Student.readLine()) != null)
            {
                id = s.substring(3,s.indexOf("Name") - 2);
                name = s.substring(s.indexOf("Name") + 5, s.indexOf("Password") - 2);
                password = s.substring(s.indexOf("Password") + 9, s.length());

                BasicData obj = new BasicData(Integer.parseInt(id), name, password);
                Student.add(obj);

            }

            in_Admin.close();
            in_Student.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }


    public boolean AdminLogin()
    {
        Scanner in = new Scanner(System.in);
        String name;
        String password;

        System.out.print("\nEnter Admin's Name: ");
        name = in.nextLine();
        System.out.print("Enter Admin's Password: ");
        password = in.nextLine();

        for(int i = 0; i<Admin.size();i++)
        {
            if(Admin.get(i).getName().equals(name) && Admin.get(i).getPassword().equals(password))
                return true;
        }
        return false;
    }


    public boolean StudentLogin()
    {
        Scanner in = new Scanner(System.in);
        int sID;
        String password;

        System.out.print("\nEnter Student's ID: ");
        sID = in.nextInt();
        in.nextLine();
        System.out.print("Enter Student's Password: ");
        password = in.next();

        for(int i = 0; i<Student.size();i++)
        {
            if(Student.get(i).getID() == sID && Student.get(i).getPassword().equals(password))
                return true;
        }
        return false;
    }

    public void showStudentEvents()
    {
        try{
            BufferedReader in = new BufferedReader (new FileReader("event.txt"));
            String s;
            System.out.println("\nList of Events: ");

            while ((s = in.readLine()) != null)
                System.out.println("o "  + s);
        }
        catch(Exception e)
        { }
    }


    public boolean viewStudentDetails()
    {
        System.out.print("\nDetails of All Student:.");
        for(int i = 0; i<Student.size();i++)
            Student.get(i).print();
        if(Student.size() > 0)
            return true;
        return false;
    }

    public boolean searchStudentDetails(int id)
    {
        for(int i = 0; i<Student.size();i++)
        {
            if(Student.get(i).getID() == id)
            {
                Student.get(i).print();
                return true;
            }
        }
        return false;

    }

    public boolean removeStudent(int id)
    {

        for(int i = 0; i<Student.size();i++)
        {
            if(Student.get(i).getID() == id)
            {
                Student.remove(i);
                rewriteStudentFile();
                return true;
            }
        }
        return false;
    }

    public int countStudent()
    {
        return Student.size();
    }

    public static void rewriteStudentFile()
    {
        try{
            BufferedWriter out_Student = new BufferedWriter (new FileWriter("student.txt"));

            for(int i = 0; i < Student.size();i++)
            {
                out_Student.write("Id:" + Student.get(i).getID() + ", ");
                out_Student.write("Name:" + Student.get(i).getName() + ", ");
                out_Student.write("Password:" + Student.get(i).getPassword());
                out_Student.write("\n");
            }

            out_Student.close();

        }
        catch(Exception e)
        { }
    }


    public String AddStudent(){

        try {

            Scanner in = new Scanner(System.in);
            int cID;
            String cname;
            String cpassword;

            System.out.print("Enter Student's ID: ");
            cID = in.nextInt();
            in.nextLine();

            if (!searchStudentDetails(cID)) {

                System.out.print("Enter Student's Name: ");
                cname = in.nextLine();
                System.out.print("Enter Student's Password: ");
                cpassword = in.nextLine();

                ValidatePassword(cpassword);

                BasicData obj = new BasicData(cID, cname, cpassword);
                Student.add(obj);
                rewriteStudentFile();
                return "Student Added Successfully";
            }
            else
            {
                return "Student Exists";
            }
        }
        catch (Exception e)
        {
            return e.getMessage();
        }
    }

    //update from private static void to public static void
    public static void ValidatePassword(@NotNull String password) throws PasswordValidationException {
        if (password.length() != 9)
        {
            throw new PasswordValidationException("Password length should be 9", new RuntimeException());
        }
        else  if (!"p".equals(password.substring(0,1)))
        {
            throw new PasswordValidationException("First letter of the Password should be p", new RuntimeException());
        }
        else  if (!"#".equals(password.substring(8,9)))
        {
            throw new PasswordValidationException("Last letter of the password should be #", new RuntimeException());
        }
    }
}
