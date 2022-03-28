package com.example.producttest;

import android.net.Uri;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class Model {
    View view;
    TextInputLayout sizeInput, colorInput, newAmountInput , oldAmountInput;
    Uri imageUri;
    String urlString;

    public Model() {
    }

    public Model(View view, TextInputLayout sizeInput, TextInputLayout colorInput, TextInputLayout newAmountInput, TextInputLayout oldAmountInput, Uri imageUri) {
        this.view = view;
        this.sizeInput = sizeInput;
        this.colorInput = colorInput;
        this.newAmountInput = newAmountInput;
        this.oldAmountInput = oldAmountInput;
        this.imageUri = imageUri;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public TextInputLayout getSizeInput() {
        return sizeInput;
    }

    public void setSizeInput(TextInputLayout sizeInput) {
        this.sizeInput = sizeInput;
    }

    public TextInputLayout getColorInput() {
        return colorInput;
    }

    public void setColorInput(TextInputLayout colorInput) {
        this.colorInput = colorInput;
    }


    public TextInputLayout getNewAmountInput() {
        return newAmountInput;
    }

    public void setNewAmountInput(TextInputLayout newAmountInput) {
        this.newAmountInput = newAmountInput;
    }

    public TextInputLayout getOldAmountInput() {
        return oldAmountInput;
    }

    public void setOldAmountInput(TextInputLayout oldAmountInput) {
        this.oldAmountInput = oldAmountInput;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }
}
