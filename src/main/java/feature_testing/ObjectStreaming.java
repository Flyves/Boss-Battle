package feature_testing;

import util.data_structure.Indexer;
import util.math.linear_transform.LinearTransform;
import util.math.vector.Vector2;

import java.io.*;

public class ObjectStreaming {

    public static void main(String[] args) {

        try {
            // output in a file
            FileOutputStream fos = new FileOutputStream("t.tmp");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            Vector2 scale = new Vector2(3, 3);
            LinearTransform transform = new LinearTransform(scale.asArray());
            Indexer<Double> indexer = new Indexer<>(transform);

            indexer.set(new Double[]{0.0, 0.0},
                    1.0);
            indexer.set(new Double[]{0.3333334, 0.0},
                    2.0);
            indexer.set(new Double[]{0.0, 1.0},
                    3.0);
            indexer.set(new Double[]{1.0, 1.0},
                    4.0);

            oos.writeObject(indexer);

            oos.close();

            // input from a file
            FileInputStream fis = new FileInputStream("t.tmp");
            ObjectInputStream ois = new ObjectInputStream(fis);

            Indexer<Double> indexer2 = (Indexer<Double>) ois.readObject();

            ois.close();

            System.out.println(indexer2.get(new Vector2(0, 0).asArray()));
        }
        catch (IOException ioException) {
            System.out.println("io");
        }
        catch (ClassNotFoundException classNotFoundException) {
            System.out.println("class");
        }
    }

}
