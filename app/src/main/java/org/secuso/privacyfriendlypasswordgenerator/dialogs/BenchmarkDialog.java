package org.secuso.privacyfriendlypasswordgenerator.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.secuso.privacyfriendlypasswordgenerator.R;
import org.secuso.privacyfriendlypasswordgenerator.generator.PasswordGenerator;
import org.secuso.privacyfriendlypasswordgenerator.generator.PasswordGeneratorTask;

/**
 * Created by karo on 23.11.16.
 */

public class BenchmarkDialog extends DialogFragment {

    Activity activity;
    View rootView;

    int iterations;
    String hashAlgorithm;

    ProgressBar spinner;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        rootView = inflater.inflate(R.layout.dialog_benchmark, null);

        spinner = (ProgressBar) rootView.findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        Bundle bundle = getArguments();

        hashAlgorithm = bundle.getString("hash_algorithm");
        //iterations = bundle.getInt("number_iterations");

        builder.setView(rootView);
        builder.setIcon(R.mipmap.ic_drawer);
        builder.setTitle(getActivity().getString(R.string.dialog_benchmark_title));
        builder.setPositiveButton(getActivity().getString(R.string.done), null);

        final Button benchmarkButton = (Button) rootView.findViewById(R.id.benchmarkButton);
        benchmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView executionTextView = (TextView) rootView.findViewById(R.id.benchmark_execution);
                executionTextView.setText(getString(R.string.dialog_benchmark_time));

                spinner.setVisibility(View.VISIBLE);

                generatePassword(iterations, hashAlgorithm);
            }
        });

        return builder.create();
    }

    public void generatePassword(int iterations, String hashFunction) {

        double startTime = System.currentTimeMillis();

        //pack parameters to String-Array
        String[] params = new String[13];
        params[0] = "abc.com";
        params[1] = "user";
        params[2] = "masterpassword";
        params[3] = "deviceID";
        params[4] = "4242";
        params[5] = String.valueOf(4000);
        //params[5] = String.valueOf(iterations);
        params[6] = hashFunction;
        params[7] = String.valueOf(1);
        params[8] = String.valueOf(1);
        params[9] = String.valueOf(1);
        params[10] = String.valueOf(1);
        params[11] = String.valueOf(20);
        params[12] = String.valueOf(startTime);

        new PasswordGeneratorTask() {

            @Override
            protected String doInBackground(String[] strings) {

                PasswordGenerator generator = new PasswordGenerator(strings[0],
                        strings[1],
                        strings[2],
                        strings[3],
                        Integer.valueOf(strings[4]),
                        Integer.parseInt(strings[5]),
                        strings[6]);

                generator.getPassword(Integer.parseInt(strings[7]), Integer.parseInt(strings[8]), Integer.parseInt(strings[9]), Integer.parseInt(strings[10]), Integer.parseInt(strings[11]));
                return strings[12];
            }

            @Override
            protected void onPostExecute(String result) {
                TextView benchmarkTextView = (TextView) rootView.findViewById(R.id.benchmarkTextView);
                double stopTime = System.currentTimeMillis();
                spinner.setVisibility(View.GONE);
                benchmarkTextView.setText(String.valueOf((stopTime - Double.parseDouble(result)) / 1000) + " " + getString(R.string.dialog_benchmark_seconds));
            }
        }.execute(params);
    }
}



