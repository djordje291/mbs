package com.djordjeratkovic.mbs.repository;

import android.app.Application;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.djordjeratkovic.mbs.R;
import com.djordjeratkovic.mbs.model.Customer;
import com.djordjeratkovic.mbs.model.Employee;
import com.djordjeratkovic.mbs.model.User;
import com.djordjeratkovic.mbs.model.Warehouse;
import com.djordjeratkovic.mbs.ui.login_register.LoginActivity;
import com.djordjeratkovic.mbs.util.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

public class MBSRepository {
    private static final String TAG = "MBSR";

    private static MBSRepository instance = null;
    private Application application = null;

    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private MutableLiveData<Boolean> loggedOutMutableLiveData;

    private FirebaseFirestore db;
    private CollectionReference userReference;
    private CollectionReference employeeReference;
    private CollectionReference customerReference;
    private CollectionReference warehouseReference;


    public MBSRepository() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userMutableLiveData = new MutableLiveData<>();
        this.loggedOutMutableLiveData = new MutableLiveData<>();
        this.db = FirebaseFirestore.getInstance();
        this.userReference = db.collection(Constants.KEY_USER_REFERENCE);
        this.employeeReference = db.collection(Constants.KEY_EMPLOYEE_REFERENCE);
        this.customerReference = db.collection(Constants.KEY_CUSTOMER_REFERENCE);
        this.warehouseReference = db.collection(Constants.KEY_WAREHOUSE_REFERENCE);
    }

    public static MBSRepository getInstance() {
        if (instance == null) {
            instance = new MBSRepository();
        }
        return instance;
    }

    public void setApplication(Application application) {
        if (this.application == null) {
            this.application = application;
        }
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        if (firebaseAuth.getCurrentUser() != null) {
            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
            loggedOutMutableLiveData.postValue(false);
        }
        return userMutableLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutMutableLiveData;
    }

    public void logOut() {
        firebaseAuth.signOut();
        loggedOutMutableLiveData.postValue(true);
    }

    public MutableLiveData<Boolean> login(String user, String password) {
        MutableLiveData<Boolean> b = new MutableLiveData<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            firebaseAuth.signInWithEmailAndPassword(user, password)
                    .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                                b.setValue(true);
                            } else {
                                Toast.makeText(application.getApplicationContext(), application.getString(R.string.login_failed)
                                        + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                b.setValue(false);
                            }
                        }
                    });
        }
        return b;
    }

    public MutableLiveData<Boolean> createAccount(String name, String email, String password) {
        MutableLiveData<Boolean> b = new MutableLiveData<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(application.getMainExecutor()
                            , new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                                        addUser(name, email, firebaseAuth.getUid());
                                        b.setValue(true);
                                    } else {
                                        Toast.makeText(application, application.getString(R.string.register_failed)
                                                + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                        b.setValue(false);
                                    }
                                }
                            });
        }
        return b;
    }

    private void addUser(String name, String email, String userFirebaseId) {
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setUserFirebaseId(userFirebaseId);
        userReference.add(u).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "onSuccess: added user");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: failed to add user");
            }
        });
    }

    public void addEmployee(Employee employee) {
        customerReference.add(employee)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: added employee");
                        for (Warehouse warehouse : employee.getWarehouses()) {
                            addWarehouse(warehouse);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: failed to add employee");
                    }
                });
    }

    public void addCustomer(Customer customer) {
        customerReference.add(customer)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: added customer");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: failed to add customer");
                    }
                });
    }

    public void addWarehouse(Warehouse warehouse) {
        warehouseReference.add(warehouse).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "onSuccess: added warehouse");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: failed to add warehouse");
            }
        });
    }

//    public void updateEmployee(Employee employee) {
//        Map<String, Object> updates = new HashMap<>();
//        updates.put(Constants.KEY_FIRST_NAME, employee.getFirstName());
//        updates.put(Constants.KEY_LAST_NAME, employee.getLastName());
//        updates.put(Constants.KEY_CITY, employee.getCity());
//        updates.put(Constants.KEY_WAREHOUSES, employee.getWarehouses());
//        employeeReference.document(employee.getDocRef()).update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Log.d(TAG, "onSuccess: updated employee");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "onFailure: failed to update employee");
//            }
//        });
//    }

    public void updateCustomer(Customer customer) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_NAME, customer.getName());
        updates.put(Constants.KEY_EMPLOYEE_DOC_REF, customer.getEmployeeDocRef());
        updates.put(Constants.KEY_PIB, customer.getPIB());
        customerReference.document(customer.getDocRef()).update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: updated customer");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: failed to update customer");
            }
        });
    }

//    public void deleteEmployee(Employee employee) {
//        employeeReference.document(employee.getDocRef()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Log.d(TAG, "onSuccess: deleted employee");
//                //TODO: delete magacine i vidi kako ces kad ih updejtujes
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "onFailure: failed to delete employee");
//            }
//        });
//    }

    public void deleteCustomer(Customer customer) {
        customerReference.document(customer.getDocRef()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: deleted customer");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: failed to delete customer");
            }
        });
    }
}
