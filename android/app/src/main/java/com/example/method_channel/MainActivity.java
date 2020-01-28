package com.example.method_channel;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

import stone.application.StoneStart;
import stone.application.interfaces.StoneCallbackInterface;
import stone.environment.Environment;
import stone.providers.ActiveApplicationProvider;
import stone.user.UserModel;
import stone.utils.Stone;


public class MainActivity extends FlutterActivity {

  private static final String CHANNEL = "samples.flutter.dev/initiate";

  @Override
  public void configureFlutterEngine(FlutterEngine flutterEngine) {
    GeneratedPluginRegistrant.registerWith(flutterEngine);

    new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL).setMethodCallHandler(
            (call, result) -> {
              if (call.method.equals("initiateStone")) {
                int batteryLevel = initiateStone(this);

                if (batteryLevel != -1) {
                  result.success(batteryLevel);
                } else {
                  result.error("UNAVAILABLE", "Battery level not available.", null);
                }
              } else {
                result.notImplemented();
              }
            });
  }

  private int initiateStone(Context context) {
    List<UserModel> userList = StoneStart.init(context);
    Stone.setAppName("StoneDemo");
    Stone.setEnvironment(Environment.SANDBOX);

    if (userList == null) {
      ActiveApplicationProvider activeApplicationProvider = new ActiveApplicationProvider(context);
      activeApplicationProvider.setDialogMessage("Ativando o Stone Code");
      activeApplicationProvider.setDialogTitle("Aguarde");
      activeApplicationProvider.useDefaultUI(true);
      activeApplicationProvider.setConnectionCallback(new StoneCallbackInterface() {

        public void onSuccess() {
          // SDK ativado com sucesso
          Toast.makeText(context.getApplicationContext(), "Iniciado com sucesso", Toast.LENGTH_SHORT).show();
        }

        public void onError() {
          // Ocorreu algum erro na ativação
          Toast.makeText(context.getApplicationContext(), "Erro na ativação", Toast.LENGTH_SHORT).show();
        }
      });
      activeApplicationProvider.activate("248083470");
    } else {
      // O SDK já foi ativado.
      Toast.makeText(context.getApplicationContext(), "SDK já ativado", Toast.LENGTH_SHORT).show();
    }
    return 200;
  }
}
