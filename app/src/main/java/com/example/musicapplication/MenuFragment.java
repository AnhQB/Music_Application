package com.example.musicapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth mAuth;
    private Button mLoginButton;
    private Button mLogoutButton;
    private Button btn_AddMusic;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        Button button = view.findViewById(R.id.loginmenuBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();

        // Khởi tạo các thành phần tương ứng với nút đăng nhập và đăng xuất
        mLoginButton = view.findViewById(R.id.loginmenuBtn);
        mLogoutButton = view.findViewById(R.id.logoutmenuBtn);
        btn_AddMusic = view.findViewById(R.id.btn_addMusic);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.detach(MenuFragment.this);
                fragmentTransaction.attach(MenuFragment.this);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        btn_AddMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddMusic.class);
                startActivity(intent);
            }
        });

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (mAuth.getCurrentUser() != null) {
            // Người dùng đã đăng nhập
            // Ẩn nút đăng nhập và hiển thị nút đăng xuất
            mLoginButton.setVisibility(View.GONE);
            mLogoutButton.setVisibility(View.VISIBLE);
	    btn_AddMusic.setVisibility(View.VISIBLE);
        } else {
            // Người dùng chưa đăng nhập
            // Hiển thị nút đăng nhập và ẩn nút đăng xuất
            mLoginButton.setVisibility(View.VISIBLE);
            mLogoutButton.setVisibility(View.GONE);
            btn_AddMusic.setVisibility(View.GONE);
        }

        return view;
    }
}