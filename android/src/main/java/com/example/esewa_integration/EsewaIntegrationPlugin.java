package com.example.esewa_integration;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.content.Intent;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.UUID;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import com.esewa.android.sdk.payment.ESewaConfiguration;
import com.esewa.android.sdk.payment.ESewaPayment;
import com.esewa.android.sdk.payment.ESewaPaymentActivity;

/** EsewaIntegrationPlugin */
public class EsewaIntegrationPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private ESewaConfiguration eSewaConfiguration;
  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "esewa_integration");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if(call.method.equals("initPayment")){

      eSewaPaymentPortal((HashMap<String, String>) call.arguments);


    } else {
      result.notImplemented();
    }
  }
  private void eSewaPaymentPortal(HashMap<String, String> arguments) {
    eSewaConfiguration = new ESewaConfiguration()
            .clientId(arguments.get("merchent_id"))
            .secretKey(arguments.get("merchent_secret_key"))
            .environment(arguments.get("environment"));

    ESewaPayment eSewaPayment=new ESewaPayment(arguments.get("amount"), arguments.get("productName"), arguments.get("productId"), arguments.get("callbackUrl"));
    Intent intent = new Intent(this, ESewaPaymentActivity.class);
    intent.putExtra(ESewaConfiguration.ESEWA_CONFIGURATION, eSewaConfiguration);
    intent.putExtra(ESewaPayment.ESEWA_PAYMENT, eSewaPayment);
    startActivityForResult(intent, REQUEST_CODE_PAYMENT);
  }
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_PAYMENT) {
      if (resultCode == RESULT_OK) {
        String s = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE);
        this.m.invokeMethod("eSewaPaymentsuccess", s);
      } else if (resultCode == RESULT_CANCELED) {
        this.m.invokeMethod("eSewaPaymentcancled", "Payment cancled by User.");
      } else if (resultCode == ESewaPayment.RESULT_EXTRAS_INVALID) {
        String s = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE);
        this.m.invokeMethod("eSewaPaymenterror", s);
      }
      else{
        this.m.invokeMethod("unknownerror","unknown error");
      }
    }
  }
  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
