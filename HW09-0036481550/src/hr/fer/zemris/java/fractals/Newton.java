package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import hr.fer.zemris.java.fractals.complex.Complex;
import hr.fer.zemris.java.fractals.complex.ComplexPolynomial;
import hr.fer.zemris.java.fractals.complex.ComplexRootedPolynomial;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

/**
 * 
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public class Newton {

    /**
     * Starting point of a program.
     * 
     * @param args
     *            Command-line argument
     */
    public static void main(String[] args) {

        Complex[] roots = getUserInput();

        ComplexRootedPolynomial polynom = new ComplexRootedPolynomial(roots);

        FractalViewer.show(new MyProducer(polynom));
    }

    /**
     * Gets user input and returns it as an array of complex values.
     * <p>
     * Firstly welcoming messages will be printed to standard output and then
     * user can give input. If input is invalid error message will be printed.
     * User can provide input until he/she writes "done".
     * 
     * @return array of complex values that user inputed
     */
    private static Complex[] getUserInput() {
        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
        System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");

        List<Complex> roots = new ArrayList<>();

        String line = "";
        try (Scanner scan = new Scanner(System.in)) {
            while (true) {
                System.out.println("Root " + (roots.size() + 1) + "> ");
                line = scan.nextLine();

                if (line.equals("done")) {
                    if (roots.size() >= 2) {
                        break;
                    } else {
                        System.out.println("Please enter at least two roots!");
                        continue;
                    }
                }

                try {
                    roots.add(Complex.valueOf(line));
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    continue;
                }
            }
        }

        System.out.println("Image of fractal will appear shortly. Thank you.");
        return roots.toArray(new Complex[0]);
    }

    /**
     * {@code MyProducer} class generates data for visualization of the Newton
     * fractal.
     * <p>
     * This class is an impementation of the {@link IFractalProducer} interface.
     * 
     * @author Karlo Vrbić
     * @version 1.0
     * @see IFractalProducer
     */
    static class MyProducer implements IFractalProducer {

        /** Number of threads that will handle calculation. */
        private static final int NUM_OF_THREADS = Runtime.getRuntime().availableProcessors();
        /** Number of jobs that will be handed to threads. */
        private static final int NUM_OF_JOBS = NUM_OF_THREADS * 8;
        /** Convergence threshold */
        private static final double CONVERGENCE_THRESHOLD = 0.001;
        /** Root threshold. */
        private static final double ROOT_THRESHOLD = 0.002;
        /** Maximum number of iterations. */
        private static final int MAX_ITERATIONS = 16 * 16;

        /** Thread pool. */
        private ExecutorService pool;
        /** Polynom that will be used for iteration. */
        private ComplexRootedPolynomial polynom;

        /**
         * Constructs a new {@code MyProducer} object from specified polynom.
         * 
         * @param polynom
         *            polynom that will be used for iteration
         */
        public MyProducer(ComplexRootedPolynomial polynom) {
            this.pool = Executors.newFixedThreadPool(NUM_OF_THREADS, new ThreadFactory() {

                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setDaemon(true);

                    return t;
                }
            });

            this.polynom = polynom;
        }

        @Override
        public void produce(
                double reMin,
                double reMax,
                double imMin,
                double imMax,
                int width,
                int height,
                long requestNo,
                IFractalResultObserver observer) {

            class Job implements Runnable {

                int yMin;
                int yMax;
                short[] data;

                public Job(int yMin, int yMax, short[] data) {
                    super();
                    this.yMin = yMin;
                    this.yMax = yMax;
                    this.data = data;
                }

                @Override
                public void run() {
                    ComplexPolynomial derived = polynom.toComplexPolynom().derive();

                    for (int y = yMin; y <= yMax; y++) {
                        int offset = y * width;
                        for (int x = 0; x < width; x++) {
                            double real = x / (width - 1.0) * (reMax - reMin) + reMin;
                            double imag = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;

                            Complex zn = Complex.valueOf(real, imag);
                            Complex zn1 = null;

                            int iter = 0;
                            double module = 0.0;
                            do {
                                Complex numerator = polynom.apply(zn);
                                Complex denominator = derived.apply(zn);

                                Complex fraction = numerator.divide(denominator);

                                zn1 = zn.sub(fraction);
                                iter++;

                                module = zn1.sub(zn).module();

                                zn = zn1;
                            } while (module > CONVERGENCE_THRESHOLD && iter < MAX_ITERATIONS);

                            int index = polynom.indexOfClosestRootFor(zn1, ROOT_THRESHOLD);

                            if (index == -1) {
                                data[offset + x] = 0;
                            } else {
                                data[offset + x] = (short) index;
                            }
                        }
                    }
                }

            }

            List<Future<?>> rezultati = new ArrayList<>();

            int numOfYPerThread = height / NUM_OF_JOBS;
            short[] data = new short[width * height];

            for (int i = 0; i < NUM_OF_JOBS; i++) {
                int yMin = i * numOfYPerThread;
                int yMax = (i + 1) * numOfYPerThread - 1;

                if (i == NUM_OF_JOBS - 1) {
                    yMax = height - 1;
                }

                Job job = new Job(yMin, yMax, data);
                rezultati.add(pool.submit(job));
            }

            for (Future<?> posao : rezultati) {
                try {
                    posao.get();
                } catch (InterruptedException | ExecutionException e) {
                }
            }

            observer.acceptResult(data, (short) (polynom.toComplexPolynom().order() + 1), requestNo);
        }
    }

}