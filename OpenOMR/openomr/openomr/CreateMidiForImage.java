package openomr.openomr;

import openomr.ann.ANNInterrogator;
import openomr.gui.GUI;
import openomr.midi.ScoreGenerator;
import openomr.omr_engine.*;

import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.*;

/**
 * Created by kavindudiyes on 08/03/2014.
 */
public class CreateMidiForImage {

    public static void main(String[]args){
        final BufferedImage buffImage;
        String filename = args[0];
        final String outputFileName = args[1];
        GUI.setANNInterrogator(new ANNInterrogator());
        try {
            buffImage = ImageIO.read(new File(filename));

            final StaveParameters staveParameters = new StaveParameters(buffImage);
            staveParameters.calcParameters();

            System.out.printf("n1=%d, n2=%d, d1=%d, d2=%d\n", staveParameters.getN1(), staveParameters.getN2(), staveParameters.getD1(), staveParameters.getD2());


            ExecutorService executor = Executors.newCachedThreadPool();
            Callable<Object> task = new Callable<Object>() {
                public Object call() {
                    YProjection yproj = new YProjection(buffImage);
                    yproj.calcYProjection(0, buffImage.getHeight(), 0, buffImage.getWidth());

                    StaveDetection staveDetection = new StaveDetection(yproj, staveParameters);
                    staveDetection.setParameters(0.75, 0.75);
                    staveDetection.locateStaves();

                    if (staveDetection.getNumStavesFound() > 0)
                    {
                        //calculate ditsance between notes
                        staveDetection.calcNoteDistance();

                        DetectionProcessor detection = new DetectionProcessor(buffImage, staveDetection, GUI.getNeuralNetwork());
                        detection.processAll();

                        ScoreGenerator scoreGen;

                        LinkedList<Staves> staveList = staveDetection.getStaveList();
                        try {
                            scoreGen = new ScoreGenerator(staveList);
                            scoreGen.makeSong(64);
                            scoreGen.saveFile(outputFileName);
                        } catch (MidiUnavailableException e) {
                            e.printStackTrace();
                        } catch (InvalidMidiDataException e) {
                            e.printStackTrace();
                        }


                        //scoreGen.start();


                    }
                    return 0;
                }
            };
            Future<Object> future = executor.submit(task);
            try {
                Object result = future.get(15, TimeUnit.SECONDS);
            } catch (TimeoutException ex) {
                System.exit(-1);
            } catch (InterruptedException e) {
                // handle the interrupts
            } catch (ExecutionException e) {
                // handle other exceptions
            } finally {
                System.exit(0); // may or may not desire this
            }





        } catch (IOException e) {
            System.out.println("IO Exception while trying to read the image file to process");
            e.printStackTrace();
        }


        System.out.println("At the very end");
        System.exit(0);
    }
}
