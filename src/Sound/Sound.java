package Sound;



import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    public Clip clip;
    public  AudioInputStream audioInputStream;
    public Sound(String path)  {


            path = System.getProperty("user.dir") +"\\src\\Sound\\"+ path;
            try {
                 audioInputStream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
                clip = AudioSystem.getClip();

                clip.open(audioInputStream);

            } catch(Exception ex) {
                System.out.println("Error with playing sound.");

            }

    }
    public void play() {
        try
        {
            clip.start();


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void stop()
    {
        clip.stop();
    }
    public void setLoop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}