package com.example.gestionetudiant;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.gestionetudiant.model.Etudiant;
import com.example.gestionetudiant.viewmodel.EtudiantViewModel;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private EditText etNom, etPrenom, etId;
    private TextView tvDateNaissance;
    private ImageView imgEtudiant;
    private Uri imageUri;
    private EtudiantViewModel etudiantViewModel;

    private final ActivityResultLauncher<Intent> imagePicker =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    loadImage();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupViewModel();
        handleIntentExtras();
    }

    private void initViews() {
        etNom = findViewById(R.id.etNom);
        etPrenom = findViewById(R.id.etPrenom);
        etId = findViewById(R.id.etId);
        tvDateNaissance = findViewById(R.id.tvDateNaissance);
        imgEtudiant = findViewById(R.id.imgEtudiant);

        findViewById(R.id.btnDateNaissance).setOnClickListener(v -> showDatePicker());
        findViewById(R.id.btnChoisirImage).setOnClickListener(v -> openImagePicker());
        findViewById(R.id.btnAjouter).setOnClickListener(v -> addEtudiant());
        findViewById(R.id.btnRechercher).setOnClickListener(v -> searchEtudiant());
        findViewById(R.id.btnModifier).setOnClickListener(v -> updateEtudiant());
        findViewById(R.id.btnSupprimer).setOnClickListener(v -> deleteEtudiant());
        findViewById(R.id.btnLister).setOnClickListener(v -> openEtudiantList());
    }

    private void setupViewModel() {
        etudiantViewModel = new ViewModelProvider(this).get(EtudiantViewModel.class);
    }

    private void handleIntentExtras() {
        if (getIntent() != null && getIntent().hasExtra("ETUDIANT_ID")) {
            int etudiantId = getIntent().getIntExtra("ETUDIANT_ID", -1);
            if (etudiantId != -1) {
                etId.setText(String.valueOf(etudiantId));
                searchEtudiant();
            }
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = dayOfMonth + "/" + (month + 1) + "/" + year;
            tvDateNaissance.setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePicker.launch(intent);
    }

    private void loadImage() {
        if (imageUri != null) {
            Glide.with(this)
                    .load(imageUri)
                    .circleCrop()
                    .into(imgEtudiant);
        }
    }

    private void addEtudiant() {
        if (!validateInputs()) return;

        Etudiant etudiant = createEtudiantFromInputs();
        etudiantViewModel.ajouterEtudiant(etudiant).observe(this, result -> {
            if (result == 1) {
                showToast("Étudiant ajouté avec succès");
                clearFields();
            } else if (result == 0) {
                showToast("Aucun étudiant ajouté");
            } else {
                showToast("Erreur lors de l'ajout");
            }
        });
    }

    private void searchEtudiant() {
        String idText = etId.getText().toString().trim();
        if (idText.isEmpty()) {
            showToast("Veuillez entrer un ID");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            etudiantViewModel.chercherEtudiant(id).observe(this, etudiant -> {
                if (etudiant != null) {
                    populateFields(etudiant);
                } else {
                    showToast("Étudiant introuvable");
                }
            });
        } catch (NumberFormatException e) {
            showToast("ID invalide");
        }
    }

    private void updateEtudiant() {
        if (!validateInputs()) return;

        String idText = etId.getText().toString().trim();
        if (idText.isEmpty()) {
            showToast("Veuillez entrer un ID");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            Etudiant etudiant = createEtudiantFromInputs();
            etudiant.setId(id);

            etudiantViewModel.modifierEtudiant(etudiant).observe(this, result -> {
                if (result == 1) {
                    showToast("Étudiant modifié avec succès");
                } else if (result == 0) {
                    showToast("Aucune modification effectuée");
                } else {
                    showToast("Erreur lors de la modification");
                }
            });
        } catch (NumberFormatException e) {
            showToast("ID invalide");
        }
    }

    private void deleteEtudiant() {
        String idText = etId.getText().toString().trim();
        if (idText.isEmpty()) {
            showToast("Veuillez entrer un ID");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            etudiantViewModel.supprimerEtudiant(id).observe(this, result -> {
                if (result == 1) {
                    showToast("Étudiant supprimé avec succès");
                    clearFields();
                } else if (result == 0) {
                    showToast("Aucun étudiant supprimé");
                } else {
                    showToast("Erreur lors de la suppression");
                }
            });
        } catch (NumberFormatException e) {
            showToast("ID invalide");
        }
    }

    private void openEtudiantList() {
        startActivity(new Intent(this, ListeEtudiantsActivity.class));
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(etNom.getText())) {
            showToast("Veuillez entrer un nom");
            return false;
        }
        if (TextUtils.isEmpty(etPrenom.getText())) {
            showToast("Veuillez entrer un prénom");
            return false;
        }
        if (TextUtils.isEmpty(tvDateNaissance.getText())) {
            showToast("Veuillez sélectionner une date de naissance");
            return false;
        }
        return true;
    }

    private Etudiant createEtudiantFromInputs() {
        String nom = etNom.getText().toString().trim();
        String prenom = etPrenom.getText().toString().trim();
        String dateNaissance = tvDateNaissance.getText().toString().trim();
        String photoUri = (imageUri != null) ? imageUri.toString() : "";

        return new Etudiant(nom, prenom, dateNaissance, photoUri);
    }

    private void populateFields(Etudiant etudiant) {
        etNom.setText(etudiant.getNom());
        etPrenom.setText(etudiant.getPrenom());
        tvDateNaissance.setText(etudiant.getDateNaissance());

        if (!etudiant.getPhotoUri().isEmpty()) {
            imageUri = Uri.parse(etudiant.getPhotoUri());
            loadImage();
        } else {
            imgEtudiant.setImageResource(R.drawable.default_profile);
            imageUri = null;
        }
    }

    private void clearFields() {
        etNom.setText("");
        etPrenom.setText("");
        tvDateNaissance.setText("");
        etId.setText("");
        imgEtudiant.setImageResource(R.drawable.default_profile);
        imageUri = null;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}