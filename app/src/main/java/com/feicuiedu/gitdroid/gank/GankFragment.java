package com.feicuiedu.gitdroid.gank;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.feicuiedu.gitdroid.R;
import com.feicuiedu.gitdroid.Utils.ActivityUtils;
import com.feicuiedu.gitdroid.gank.model.GankItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 123 on 2016/9/1.
 */
public class GankFragment extends Fragment implements GankPresenter.GankView {

    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.btnFilter)
    ImageButton btnFilter;
    @BindView(R.id.content)
    ListView content;
    @BindView(R.id.emptyView)
    FrameLayout emptyView;

    private Date date;
    private SimpleDateFormat simpleDateFormat;
    private Calendar calendar;

    private GankPresenter gankPresenter;
    private GankAdapter adapter;

    private ActivityUtils activityUtils;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        calendar = Calendar.getInstance(Locale.CHINA);
        //获取当前的时间
        date = new Date(System.currentTimeMillis());
        gankPresenter = new GankPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gank, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        //规范我们日期格式
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        tvDate.setText(simpleDateFormat.format(date));
        adapter = new GankAdapter();
        content.setAdapter(adapter);
        content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = adapter.getItem(position).getUrl();
                activityUtils.startBrowser(url);
            }
        });

        gankPresenter.getGanks(date);
    }

    @OnClick(R.id.btnFilter)
    public void showDateDialog(View view) {

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Date这个效果是不是别人写好的
        // 是不是一个封装好了的东西
        // 也可以这样理解：是不是一个封装好了的“模块”
        // 但是这个“模块”是不是也得和外界交互？
        //   如：它事情做完后，是不是需要通过这个调用者?
        //       它的一些事件，是不是需要通过这个调用者?
        // 在这里就是：
        //  你一按下，确定，DatePickerDialog就会回调这个接口的指定方法

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        getContext(),
                        dateSetListener,
                        year, month, day);
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        //当我们选择了日期，就会触发，也就是说日期发生了变化
        @Override
        public void onDateSet(
                DatePicker view,
                int year, int monthOfYear, int dayOfMonth) {
            calendar.set(year, monthOfYear, dayOfMonth);
            date = calendar.getTime();

            tvDate.setText(simpleDateFormat.format(date));
            //更新了日期，重新执行业务，重载加载数据
            gankPresenter.getGanks(date);
        }
    };

    // 当业务那边的确获取到数据的话
    // 会来callback这里的
    // ????
    @Override
    public void setData(List<GankItem> list) {
        adapter.setDatas(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void hideEmptyView() {
        emptyView.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
    }
}
