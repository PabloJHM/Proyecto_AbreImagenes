package com.example.pablo.proyecto_abreimagenes;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
    private ImageView iv;
    private Bitmap bitmap,original;
    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        //Recogemos la imagen del intent y la mostramos en el imageView
        Uri imageUri = getIntent().getData();
        iv.setImageURI(imageUri);

        //Pasamos la imagen a bitmap y la guardamos en una variable para su posterior manejo. Tambien
        //la guardamos una copia de "seguridad" para volver a la imagen original si el usuario lo desea
        bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        original=bitmap;
    }

    public void init(){
        iv=(ImageView)findViewById(R.id.imageView);
        ll=(LinearLayout)findViewById(R.id.llEditar);
        ll.setVisibility(View.GONE);
    }

    /************************************OnClicks de los iconos************************************/
    public void desaturar(View v){
        bitmap= blancoNegro(bitmap);
        iv.setImageBitmap(bitmap);
    }

    public void invertir(View v){
        bitmap = espejo();
        iv.setImageBitmap(bitmap);
    }

    public void invertirColores(View v){
        bitmap=invertColores(bitmap);
        iv.setImageBitmap(bitmap);
    }
    public void girarDer(View v){
        bitmap= rotar(bitmap, 90);
        iv.setImageBitmap(bitmap);
    }

    public void girarIzq(View v){
        bitmap= rotar(bitmap, -90);
        iv.setImageBitmap(bitmap);
    }

    public void restaurar(View v){
        iv.setImageBitmap(original);
        bitmap=original;
    }

    //Al pulsar sobre el pincel nos aparecerá una barra de herramientas para editar la imagen
    public void mostrarHerramientas(View v){
        if(ll.getVisibility()==View.GONE)
            ll.setVisibility(View.VISIBLE);
        else
            ll.setVisibility(View.GONE);
    }
    /*****************************Aplicaciones para modificar la imagen****************************/
    public Bitmap blancoNegro(Bitmap bmpOriginal) {
        // Creamos un bitmap con las mismas caracteristicas que el original
        Bitmap bmpGris = Bitmap.createBitmap(bmpOriginal.getWidth(),
                bmpOriginal.getHeight(), Bitmap.Config.ARGB_8888);
        //Ponemos la saturación a 0, y aplicamos el filtro en el lienzo
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter cmcf = new ColorMatrixColorFilter(cm);
        Paint paint = new Paint();
        paint.setColorFilter(cmcf);
        //Dibujamos el lienzo y devolvemos la imagen
        Canvas lienzo = new Canvas(bmpGris);
        lienzo.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGris;
    }

    public Bitmap rotar(Bitmap bmpOriginal, float angulo) {
        Matrix matriz = new Matrix();
        //Rotamos la imagen segun el usuario pulsa rotar izq o rotar der
        matriz.postRotate(angulo);
        return Bitmap.createBitmap(bmpOriginal, 0, 0,
                bmpOriginal.getWidth(), bmpOriginal.getHeight(), matriz, true);
    }

    public Bitmap espejo() {
        // Creamos un bitmap con las mismas caracteristicas que el original
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        int pixel, red, green, blue, alpha;
        //Recorremos todos los pixeles de la imagen
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                pixel = bitmap.getPixel(i, j);
                red = Color.red(pixel);
                green = Color.green(pixel);
                blue = Color.blue(pixel);
                alpha = Color.alpha(pixel);
                //Pixel a pixel invertimos sus posiciones y recreamos la imagen ya invertida
                bmp.setPixel(bitmap.getWidth() - i - 1, j, Color.argb(alpha, red, green, blue));
            }
        }
        return bmp;
    }

    public  Bitmap invertColores(Bitmap bmpOriginal) {
        int height = bmpOriginal.getHeight();
        int width = bmpOriginal.getWidth();
        // Creamos un bitmap con las mismas caracteristicas que el original
        Bitmap bmpInvertido = Bitmap.createBitmap(width, height, bmpOriginal.getConfig());
        int red, green, blue, alpha;
        int pixelColor;

        //Recorremos todos los pixeles de la imagen
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixelColor = bmpOriginal.getPixel(x, y);
                alpha = Color.alpha(pixelColor);
                //Pixel a pixel invertimos sus colores primarios
                red = 255 - Color.red(pixelColor);
                green = 255 - Color.green(pixelColor);
                blue = 255 - Color.blue(pixelColor);
                //Recreamos pixel a pixel la imagen con los colores invertidos
                bmpInvertido.setPixel(x, y, Color.argb(alpha, red, green, blue));
            }
        }
        return bmpInvertido;
    }


}
