package mx.wedevelop.guernica;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import mx.wedevelop.guernica.sqlite.model.User;
import mx.wedevelop.guernica.utils.signin.SignInActivity;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends SignInActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private ProgressBar mProgressBar;

    GuernicaController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        controller = GuernicaController.getController(this);

        // Other buttons
        findViewById(R.id.button_google_sign_in).setOnClickListener(this);

        mProgressBar = (ProgressBar) findViewById(R.id.login_progress);
    }

    @Override
    public void onLogin(GoogleSignInAccount gsa) {
        User user = new User(gsa.getDisplayName(), gsa.getEmail(), gsa.getPhotoUrl() != null ? gsa.getPhotoUrl().toString() : "");
        controller.loginUser(user);

        findViewById(R.id.button_google_sign_in).setVisibility(View.GONE);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSignOut() {
        findViewById(R.id.button_google_sign_in).setVisibility(View.INVISIBLE);
        findViewById(R.id.button_google_sign_in).setVisibility(View.INVISIBLE);
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSignInRequired() {
        findViewById(R.id.button_google_sign_in).setVisibility(View.VISIBLE);
    }

    @Override
    public void onResolutionRequired() {
        findViewById(R.id.button_google_sign_in).setVisibility(View.VISIBLE);
        // Getting credential needs to show some UI, start resolution
        //resolveResult(status, RC_CREDENTIALS_READ);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_google_sign_in:
                onGoogleSignInClicked();
                break;
//            case R.id.button_google_revoke:
//                onGoogleRevokeClicked();
//                break;
//            case R.id.button_google_sign_out:
//                onGoogleSignOutClicked();
//                break;
//            case R.id.button_email_sign_in:
//                onEmailSignInClicked();
//                break;
//            case R.id.button_email_save:
//                onEmailSaveClicked();
//                break;
        }
    }

}
