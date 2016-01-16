package at.uibk.ac.at.Task3;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) {

        List<Integer> objectNums = new LinkedList<>();

        objectNums.add(1);
        objectNums.add(2);
        objectNums.add(4);
        objectNums.add(80);
        objectNums.add(160);
        objectNums.add(320);
        objectNums.add(1024);
        objectNums.add(2048);

        final int iterationsPerObject = 500;

        CalcPi calcPi = new CalcPi();
        calcPi.initialize();

        try {
            calcPi.calc(objectNums.get(objectNums.size()-1), iterationsPerObject);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int num : objectNums)
        {
            System.out.format("Number of objects: %d\n", num);

            try
            {
                calcPi.calcAndProfile(num, iterationsPerObject);
                BigDecimal pi = calcPi.calcAndProfileWithoutInstantiation(num, iterationsPerObject);

                System.out.format("Result: %f\n", pi);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            System.out.println();
        }

        calcPi.uninitialize();
    }
}
