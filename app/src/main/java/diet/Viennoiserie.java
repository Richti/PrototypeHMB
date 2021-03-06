package diet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import async.AsyncResponse;
import async.PostResponseAsyncTask;
import efrei.healthymb.R;

import static java.lang.Double.parseDouble;

/**
 * Created by Valentin on 09/04/2016.
 */
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class Viennoiserie extends AppCompatActivity implements View.OnClickListener{
    private SeekBar croissantBar, pain_chocBar, chaussonsBar,
            pain_raisinBar, pain_laitBar, briocheBar;
    private TextView croissant_Portion, pain_choc_Portion, chaussons_Portion, test_bdd,
            pain_raisin_Portion, pain_lait_Portion, brioche_Portion ;
    private TextView d_croissant_Id, d_pain_au_chocolat_Id, d_chaussons_Id, d_pain_aux_raisins_Id,
            d_pain_au_lait_Id, d_brioche_Id;

    private Double portion;
    private String data[];
    private HashMap<TextView, TextView> alimentConsomés;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viennoiserie);
        initializeVariables();
        initializeListener();
        initializeData();

    }
    private void initializeVariables() {
        croissantBar = (SeekBar) findViewById(R.id.d_croisaant_BarId);
        pain_chocBar = (SeekBar) findViewById(R.id.d_pain_au_chocolat_Bar_Id);
        chaussonsBar = (SeekBar) findViewById(R.id.d_chaussons_Bar_Id);
        pain_raisinBar =(SeekBar) findViewById(R.id.d_pain_au_raisins_Bar_Id);
        pain_laitBar = (SeekBar) findViewById(R.id.d_pain_au_lait_Bar_Id);
        briocheBar = (SeekBar) findViewById(R.id.d_brioche_Bar_Id);
        croissant_Portion = (TextView)findViewById(R.id.d_croissant_portion_Id);
        pain_choc_Portion = (TextView)findViewById(R.id.d_pain_au_chocolat_portion_Id);
        chaussons_Portion = (TextView)findViewById(R.id.d_chaussons_portion_Id);
        pain_raisin_Portion = (TextView)findViewById(R.id.d_pain_au_raisins_portion_Id);
        pain_lait_Portion = (TextView)findViewById(R.id.d_pain_au_lait_portion_Id);
        brioche_Portion = (TextView)findViewById(R.id.d_brioche_portion_Id);
        d_croissant_Id = (TextView)findViewById(R.id.d_croissantId);
        d_pain_au_chocolat_Id = (TextView)findViewById(R.id.d_pain_au_chocolat_Id);
        d_chaussons_Id = (TextView)findViewById(R.id.d_chaussons_Id);
        d_pain_au_lait_Id = (TextView)findViewById(R.id.d_pain_au_chocolat_Id);
        d_pain_aux_raisins_Id = (TextView)findViewById(R.id.d_pain_au_raisins_Id);
        d_brioche_Id = (TextView)findViewById(R.id.d_brioche_Id);
        alimentConsomés = new HashMap<TextView, TextView>();
        alimentConsomés.put(d_croissant_Id, croissant_Portion);
        alimentConsomés.put(d_pain_au_chocolat_Id, pain_choc_Portion);
        alimentConsomés.put(d_pain_au_lait_Id, pain_lait_Portion);
        alimentConsomés.put(d_brioche_Id, brioche_Portion);
        alimentConsomés.put(d_pain_aux_raisins_Id, pain_raisin_Portion);
        croissant_Portion.setText("0.0 portion");
        pain_choc_Portion.setText("0.0 portion");
        pain_raisin_Portion.setText("0.0 portion");
        pain_lait_Portion.setText("0.0 portion");
        brioche_Portion.setText("0.0 portion");


    }
    private void initializeListener() {

        croissantBar.setOnSeekBarChangeListener(new seekBarListener(croissant_Portion));
        pain_chocBar.setOnSeekBarChangeListener(new seekBarListener(pain_choc_Portion));
        chaussonsBar.setOnSeekBarChangeListener(new seekBarListener(chaussons_Portion));
        pain_raisinBar.setOnSeekBarChangeListener(new seekBarListener(pain_raisin_Portion));
        pain_laitBar.setOnSeekBarChangeListener(new seekBarListener(pain_lait_Portion));
        briocheBar.setOnSeekBarChangeListener(new seekBarListener(brioche_Portion));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.d_valider: {

                for(TextView key : alimentConsomés.keySet()){
                    if(!alimentConsomés.get(key).getText().toString().equals("0.0 portion")){
                        for(int i = 0; i< data.length; i++){
                            if(data[i].equals(key.getText().toString())){

                                portion = parseDouble(alimentConsomés.get(key).getText().toString().substring(0,2));
                                Double portionMangé = portion*parseInt(data[i+1]);
                                Aliment aliment = new Aliment(parseInt(data[i - 1]),
                                        data[i],portionMangé.intValue(),data[i+2]);

                                ((syntheseRepas)getApplication()).addAlimentPetitDejeuner(aliment, portion);

                            }

                        }
                    }

                }
                Toast.makeText(Viennoiserie.this, "Repas enregistré!!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), Petit_Dejeuner.class);
                startActivity(intent);
            }

            break;
            case R.id.d_finish: {
                Intent intent = new Intent(getApplicationContext(), resumeRepas.class);
                startActivity(intent);
            }
            break;
            case R.id.d_modifier: {
                Intent intent = new Intent(getApplicationContext(), Viennoiserie.class);
                startActivity(intent);
            }
            break;
            default:
                break;
        }


    }
    private void initializeData() {

        Bundle categorieExtras = getIntent().getExtras();
        String categorie = categorieExtras.getString("categorie");

        HashMap postData = new HashMap();
        postData.put("categorie", categorie);

        PostResponseAsyncTask task1 = new PostResponseAsyncTask(Viennoiserie.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (s.contains("success")) {
                    data = s.split(";");

                    d_croissant_Id.setText(data[2]);
                    d_pain_au_chocolat_Id.setText(data[6]);
                    d_pain_aux_raisins_Id.setText(data[10]);
                    d_pain_au_lait_Id.setText(data[14]);
                    d_brioche_Id.setText(data[18]);

                }
            }
        });
        task1.execute("http://healthymb.no-ip.org:8080/PA8/aliment.php");
    }
    class seekBarListener implements SeekBar.OnSeekBarChangeListener {
        private int progress = 0;
        private TextView textView;
        private String unite;

        public seekBarListener(TextView textView) {
            this.textView = textView;
            this.unite = textView.getText().toString().substring(1);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
            progress = progressValue;
            textView.setText(progress + unite);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            textView.setText(progress + unite);
        }
    }
}