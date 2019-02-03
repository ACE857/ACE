package com.example.ace.ace.FundRaising;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.ace.ace.ModelAndAdapters.*;
import com.example.ace.ace.ModelAndAdapters.UserDataModel;
import com.example.ace.ace.R;

public class fundTransfer extends AppCompatActivity {

    private String TAG = "fundTransfer";
    donationClassModel dm ;
    UserDataModel user;
    private String payeeAddress = "amit220698@okhdfcbank";       //9325490843@paytm
    private String payeeName = "Kuldeep Singh";
    private String transactionNote = "Donation";
    private String amount = "1";
    EditText amt;
    private String currencyUnit = "INR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_transfer);
        amt = findViewById(R.id.donationamount);
        user = (UserDataModel) getIntent().getSerializableExtra("user");        //  details of the user that created the petetion
        dm = (donationClassModel) getIntent().getSerializableExtra("tran");        //  details of the user that created the petetion
        Button payButton = findViewById(R.id.payfund);
        amount = amt.getText()+"";
        payeeName = dm.name;
        payeeAddress = dm.upiID;
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("upi://pay?pa="+payeeAddress+"&pn="+payeeName+"&tn="+transactionNote+
                        "&am="+amount+"&cu="+currencyUnit);

                /*
                    consult this link for further elaboration on deeplinking:-
                        https://stackoverflow.com/questions/44985944/upi-app-deep-linking-using-intent-inconsistent-and-buggy-behavior
                 */

                Log.d(TAG, "onClick: uri: "+uri);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivityForResult(intent,1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: requestCode: "+requestCode);
        Log.d(TAG, "onActivityResult: resultCode: "+resultCode);
        //txnId=UPI20b6226edaef4c139ed7cc38710095a3&responseCode=00&ApprovalRefNo=null&Status=SUCCESS&txnRef=undefined
        //txnId=UPI608f070ee644467aa78d1ccf5c9ce39b&responseCode=ZM&ApprovalRefNo=null&Status=FAILURE&txnRef=undefined

        if(data!=null) {
            Log.d(TAG, "onActivityResult: data: " + data.getStringExtra("response"));
            String res = data.getStringExtra("response");
            String search = "SUCCESS";
            if (res.toLowerCase().contains(search.toLowerCase())) {
                Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Payment Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}