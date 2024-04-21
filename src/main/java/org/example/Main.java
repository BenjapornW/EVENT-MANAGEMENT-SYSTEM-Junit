package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String args[])
    {
        try{
            Scanner in = new Scanner(System.in);
            Event ems = new Event();
            int choice = 0;

            while(choice !=3)
            {
                System.out.println("\n\n********************************* WELCOME TO THE EVENT MANAGEMENT SYSTEM *********************************** ");
                System.out.println("\n\nDo you want to proceed as an Admin or a Student? ");
                System.out.println("\nChoose wisely: ");
                System.out.println("1 - Admin");
                System.out.println("2 - Student");
                System.out.println("3 - Exit");
                System.out.print("\nEnter your choice: ");


                choice = in.nextInt();
                if(choice == 1)
                {
                    System.out.println("\nNOTE: Check respective file for getting credentials");
                    if(ems.AdminLogin())
                    {
                        int c = 0;
                        while (c != 6)
                        {
                            System.out.print("\n1 - View details of all Students");
                            System.out.print("\n2 - Search details of a specific Student");
                            System.out.print("\n3 - Delete a Student");
                            System.out.print("\n4 - Insert a Student");
                            System.out.print("\n5 - Counts the number of Students");
                            System.out.print("\n6 - Logout");
                            System.out.print("\nEnter the function you want to perform: ");
                            c = in.nextInt();

                            if(c == 1)
                            {
                                if(!ems.viewStudentDetails())
                                    System.out.println("\nNo Student exists");
                            }
                            else if(c == 2)
                            {
                                System.out.println("Enter id of the specific Student you want to search details of: ");
                                System.out.println("Search result : "+ ems.searchStudentDetails(in.nextInt()));
                            }
                            else if(c == 3)
                            {
                                System.out.println("Enter id of Student you want to remove: ");
                                System.out.println("Search result : "+ ems.removeStudent(in.nextInt()));
                            }
                            else if(c == 4)
                            {
                                System.out.println(ems.AddStudent());
                            }
                            else if(c == 5)
                            {
                                System.out.println("Total Number of Students = " + ems.countStudent());
                            }
                            else if(c == 6);
                            else
                                System.out.println("\nWrong functionality chosen");
                        }
                    }
                    else
                        System.out.println("\nUnable to authenticate, try again");

                }
                else if(choice == 2)
                {
                    System.out.println("\nNOTE: Check respective file for getting credentials");
                    if(ems.StudentLogin())
                    {
                        ems.showStudentEvents();
                        System.out.print("\nPress any key and then hit enter to log out: ");
                        String key = in.next();
                    }
                    else
                        System.out.println("\nUnable to authenticate, Student deleted or perhaps it doesn't exist, but try again.");

                }
                else if(choice == 3)
                {
                    System.out.println("\nThanks for using, Bye!");
                    break;
                }
                else
                    System.out.println("\nWrong choice, Choose again.");
            }
        }
        catch(Exception e)
        {
            System.out.println("\n*Sorry we encountered an unusual error, please try again*");
        }
    }
}