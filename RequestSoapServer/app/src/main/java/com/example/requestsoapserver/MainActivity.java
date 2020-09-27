package com.example.requestsoapserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.icu.util.ULocale;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String NAMESPACE = "tis-brno.cz/labContrWs/";
    private static String METHOD = "doBsTest";
    private static String SOAP = "tis-brno.cz/labContrWs/doBsTest";
    private static String URL = "http://192.168.99.7:8080/labContrWs/LabContrWs?wsdl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new CallWebService().execute();
    }

    private class CallWebService extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";

            SoapObject request = new SoapObject(NAMESPACE, METHOD);

            String n0 = "n0:";

            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName(n0 + "idPat");
            propertyInfo.setValue("16");
            propertyInfo.setType(String.class);
            request.addProperty(propertyInfo);

            propertyInfo = new PropertyInfo();
            propertyInfo.setName(n0 + "idReq");
            propertyInfo.setValue("16");
            propertyInfo.setType(int.class);
            request.addProperty(propertyInfo);

            propertyInfo = new PropertyInfo();
            propertyInfo.setName(n0 + "pnum");
            propertyInfo.setValue("=C20191900100110");
            propertyInfo.setType(String.class);

            request.addProperty(propertyInfo);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = false;
            envelope.setOutputSoapObject(request);

            List<HeaderProperty> hl = new ArrayList<HeaderProperty>();
            hl.add(new HeaderProperty("Content-Type", "text/xml; charset=UTF-8"));

            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.debug = true;
                androidHttpTransport.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
                androidHttpTransport.call(SOAP, envelope, hl);
                SoapObject Result = (SoapObject)envelope.bodyIn;


                Object object = Result.getProperty("result");

                Log.d("RequestResult1", Result.toString());
                Log.d("RequestResult2", object.toString());

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("RequestResult3", "" + e);
            }

            return result;
        }
    }
}
