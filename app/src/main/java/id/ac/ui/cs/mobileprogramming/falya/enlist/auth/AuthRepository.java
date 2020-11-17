package id.ac.ui.cs.mobileprogramming.falya.enlist.auth;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import static id.ac.ui.cs.mobileprogramming.falya.enlist.utils.HelperClass.logErrorMessage;

@SuppressWarnings("ConstantConditions")
class AuthRepository {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = rootRef.collection("users");

    MutableLiveData<User> firebaseSignInWithGoogle(AuthCredential googleAuthCredential) {
        final MutableLiveData<User> authenticatedUserMutableLiveData = new MutableLiveData<>();
        firebaseAuth.signInWithCredential(googleAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> authTask) {
                if (authTask.isSuccessful()) {
                    boolean isNewUser = authTask.getResult().getAdditionalUserInfo().isNewUser();
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        String uid = firebaseUser.getUid();
                        String name = firebaseUser.getDisplayName();
                        String email = firebaseUser.getEmail();
                        User user = new User(uid, name, email);
                        user.isNew = isNewUser;
                        authenticatedUserMutableLiveData.setValue(user);
                    }
                } else {
                    logErrorMessage(authTask.getException().getMessage());
                }
            }
        });
        return authenticatedUserMutableLiveData;
    }

    MutableLiveData<User> createUserInFirestoreIfNotExists(final User authenticatedUser) {
        final MutableLiveData<User> newUserMutableLiveData = new MutableLiveData<>();
        final DocumentReference uidRef = usersRef.document(authenticatedUser.uid);
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> uidTask) {
                if (uidTask.isSuccessful()) {
                    DocumentSnapshot document = uidTask.getResult();
                    if (!document.exists()) {
                        uidRef.set(authenticatedUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> userCreationTask) {
                                if (userCreationTask.isSuccessful()) {
                                    authenticatedUser.isCreated = true;
                                    newUserMutableLiveData.setValue(authenticatedUser);
                                } else {
                                    logErrorMessage(userCreationTask.getException().getMessage());
                                }
                            }
                        });
                    } else {
                        newUserMutableLiveData.setValue(authenticatedUser);
                    }
                } else {
                    logErrorMessage(uidTask.getException().getMessage());
                }
            }
        });
        return newUserMutableLiveData;
    }
}
