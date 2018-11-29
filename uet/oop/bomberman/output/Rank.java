package uet.oop.bomberman.output;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Rank {


   public ArrayList <Player> array;

    public Rank()  {
        array = new ArrayList<Player>();
        File file = new File("src\\bin\\Rank.txt");
        String s = "";

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            String temp;
            String temp2[];
            while ((temp = reader.readLine()) != null) {
                temp2 = temp.split(" ");
                Player t = new Player(temp2[0], temp2[1] );
                array.add(t);
            }
            reader.close();
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void Insert(Player player)
    {
        for(int i =0; i < array.size(); i++)
        {

            if (player.getPoints().compareTo(array.get(i).getPoints()) >= 0 )
            {
                array.add(i,player);
                array.remove(array.size()-1);
                return;
            }
        }
    }
    public void XuatArr()
    {
        FileWriter fileWriter;
        BufferedWriter writer;
        try {
            File file = new File("src\\bin\\Rank.txt");
            fileWriter = new FileWriter(file);
            writer = new BufferedWriter(fileWriter);
            for (int i = 0; i< array.size();i++)
            {
                writer.write(array.get(i).getName() + " "+array.get(i).getPoints()+"\n");
            }

            writer.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
