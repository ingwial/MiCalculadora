package com.example.micalculadora;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.etInput)
    EditText etInput;
    @BindView(R.id.contentMain)
    RelativeLayout contentMain;
    @BindView(R.id.tilInput)
    TextInputLayout tilInput;
    @BindView(R.id.btnSiete)
    Button btnSiete;
    @BindView(R.id.btnCuatro)
    Button btnCuatro;
    @BindView(R.id.btnUno)
    Button btnUno;
    @BindView(R.id.llMainLeftInput)
    LinearLayout llMainLeftInput;
    @BindView(R.id.btnOcho)
    Button btnOcho;
    @BindView(R.id.btnCinco)
    Button btnCinco;
    @BindView(R.id.btnDos)
    Button btnDos;
    @BindView(R.id.llMainRigth)
    LinearLayout llMainRigth;
    @BindView(R.id.btnNueve)
    Button btnNueve;
    @BindView(R.id.btnSeis)
    Button btnSeis;
    @BindView(R.id.btnTres)
    Button btnTres;
    @BindView(R.id.btnLimpiar)
    Button btnLimpiar;
    @BindView(R.id.btnPunto)
    Button btnPunto;
    @BindView(R.id.btnCero)
    Button btnCero;
    @BindView(R.id.btnDiv)
    Button btnDiv;
    @BindView(R.id.btnMultiplicacion)
    Button btnMultiplicacion;
    @BindView(R.id.llMiddle)
    LinearLayout llMiddle;
    @BindView(R.id.btnSub)
    Button btnSub;
    @BindView(R.id.btnSum)
    Button btnSum;
    @BindView(R.id.llCenter)
    LinearLayout llCenter;
    @BindView(R.id.btnResultado)
    Button btnResultado;

    private boolean isEditInInProgress = false;
    private int minLength;
    private int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        minLength = getResources().getInteger(R.integer.main_min_length);
        textSize = getResources().getInteger(R.integer.main_input_textSize);
        configEditText();
    }

    private void configEditText() {

        etInput.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if (motionEvent.getRawX()>=etInput.getRight() -
                            etInput.getCompoundDrawables()[Constantes.DRAWABLE_RIGHT].getBounds().width()){
                        if(etInput.length()>0){
                            final int length = etInput.getText().length();
                            etInput.getText().delete(length-1, length);
                        }
                    }
                    return true;
                }
                return false;
            }

        });

        etInput.addTextChangedListener(new TextWatcher() {
               @Override
               public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

               }

               @Override
               public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    if (!isEditInInProgress &&
                            Metodos.canReplaceOperator(charSequence)){
                        isEditInInProgress = true;
                        etInput.getText().delete(etInput.getText().length()-2, etInput.getText().length()-1);
                    }

                    if (charSequence.length() > minLength){
                        etInput.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize -
                                (( charSequence.length() -minLength) *2 ) + (charSequence.length() - minLength));
                    }else{
                        etInput.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                    }
               }

               @Override
               public void afterTextChanged(Editable s) {
                   isEditInInProgress =false;
               }
        });

    }

    @OnClick({R.id.btnSiete, R.id.btnCuatro, R.id.btnUno, R.id.btnOcho, R.id.btnCinco, R.id.btnDos,
            R.id.btnNueve, R.id.btnSeis, R.id.btnTres, R.id.btnPunto, R.id.btnCero})
    public void onClickNumbers(View view) {
        final String valStr = ((Button) view).getText().toString();
        switch (view.getId()) {
            case R.id.btnCero:
            case R.id.btnUno:
            case R.id.btnDos:
            case R.id.btnTres:
            case R.id.btnCuatro:
            case R.id.btnCinco:
            case R.id.btnSeis:
            case R.id.btnSiete:
            case R.id.btnOcho:
            case R.id.btnNueve:
                etInput.getText().append(valStr);
                break;
            case R.id.btnPunto:
                final String operacion = etInput.getText().toString();

                final String operador = Metodos.getOperator(operacion);

                final int count = operacion.length() - operacion.replace(".", "").length();

                if (!operacion.contains(Constantes.POINT) ||
                        count < 2 && (!operacion.equals(Constantes.OPERATOR_NULL))) {
                    etInput.getText().append(valStr);
                }
                break;
        }
    }

    @OnClick({R.id.btnLimpiar, R.id.btnDiv, R.id.btnMultiplicacion, R.id.btnSub, R.id.btnSum, R.id.btnResultado})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnLimpiar:
                etInput.setText("");
                break;
            case R.id.btnDiv:
            case R.id.btnMultiplicacion:
            case R.id.btnSub:
            case R.id.btnSum:
                resultado(false);

                final String operador = ((Button) view).getText().toString();
                final String operacion = etInput.getText().toString();
                final String ultimoCaracter = (operacion.isEmpty()? "": operacion.substring(operacion.length() - 1));
                if (operador.equals(Constantes.OPERATOR_SUB)){
                    if (operacion.isEmpty() || (!ultimoCaracter.equals(Constantes.OPERATOR_SUB) &&
                    !ultimoCaracter.equals(Constantes.POINT))){
                        etInput.getText().append(operador);
                    }
                }else{
                    if (!operacion.isEmpty() && !(ultimoCaracter.equals(Constantes.OPERATOR_SUB)) &&
                            !(ultimoCaracter.equals(Constantes.POINT))){
                        etInput.getText().append(operador);
                    }
                }
                break;
            case R.id.btnResultado:
                resultado(true);
                break;
        }
    }

    private void resultado(boolean deResultado){
        Metodos.tryResolve(deResultado, etInput, new OnResolveCallback() {
            @Override
            public void onShowMessage(int errorRes) {
                ShowMessage(errorRes);
            }

            @Override
            public void onIsEditing() {
                isEditInInProgress = true;
            }
        });

    }

    private void ShowMessage(int errorRes) {
        Snackbar.make(contentMain, errorRes, Snackbar.LENGTH_SHORT).show();
    }
}