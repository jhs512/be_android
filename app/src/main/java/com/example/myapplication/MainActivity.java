package com.example.myapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.3:8085")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        BeApiService beApiService = retrofit.create(BeApiService.class);
        Observable<ResultData<BeApi__UsrArticle__getArticles__Body>> observable__UsrArticle__getArticlesResultData = beApiService.UsrArticle__getArticles();

        mCompositeDisposable.add(observable__UsrArticle__getArticlesResultData.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(resultData -> {
            Toast.makeText(getApplicationContext(), resultData.body.articles.get(0).getTitle() + "개", Toast.LENGTH_SHORT).show();
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }
}