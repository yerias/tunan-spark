package com.tunan.java.io.file;

import java.io.File;

public class MakeDirectories {

    private static void usage() {
        System.err.println(
                "Usage: MakeDirectories path1 ...\n" +
                        "Create each path\n" +
                        "Usage: MakeDirectories -d Path1 ...\n" +
                        "Deletes each path\n" +
                        "Usage: MakeDirectories -r path1 path2 \n" +
                        "Renames from path1 to path2");
        System.exit(1);
    }

    private static void fileDate(File f) {
        System.err.println(
                "Absolute path: " + f.getAbsolutePath() +
                        "\n Can read: " + f.canRead() +
                        "\n Can write: " + f.canWrite() +
                        "\n getName: " + f.getName() +
                        "\n getParent: " + f.getParent() +
                        "\n getPath: " + f.getPath() +
                        "\n length: " + f.length() +
                        "\n lastModified: " + f.lastModified()
        );

        if(f.isFile()){
            System.out.println("It's a file");
        }else{
            System.out.println("It's a directory");
        }
    }

    public static void main(String[] args) {
        if(args.length < 1)  {
            usage();
        }

        if (args[0].equals("-r")){
            if(args.length != 3){
                usage();
            }

            File
                    oldName = new File(args[1]),
                    newName = new File(args[2]);

            oldName.renameTo(newName);

            fileDate(oldName);
            fileDate(newName);

            return ;
        }

        int count = 0;
        boolean del = false;
        if(args[0].equals("-d")){
            count++;
            del = true;
        }

        count --;

        while (++count < args.length){
            File file = new File(args[count]);

            if(file.exists()){
                System.out.println(file + " exists");

                if(del){
                    System.out.println("deleting ...");

                    file.delete();
                }
            }else{
                if(!del){
                    file.mkdir();
                    System.out.println("create "+file);
                }
            }

            fileDate(file);
        }
    }


}
