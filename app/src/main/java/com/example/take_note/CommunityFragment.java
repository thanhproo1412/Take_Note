package com.example.take_note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommunityFragment extends Fragment {

    private RecyclerView recyclerViewPosts;
    private Button btnPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_community, container, false);

        recyclerViewPosts = rootView.findViewById(R.id.recyclerViewPosts);
        btnPost = rootView.findViewById(R.id.btnPost);

        // Create sample post list
        ArrayList<Post> mockPosts = new ArrayList<>();
        mockPosts.add(new Post("Mai Anh", "Hôm nay thật đẹp trời ☀️", R.drawable.image1, "5 phút trước"));
        mockPosts.add(new Post("Nguyễn Minh", "Vừa hoàn thành bài tập xong 💪", 0, "30 phút trước"));
        mockPosts.add(new Post("Trâm", "Chia sẻ một tấm ảnh đẹp ❤️", R.drawable.image2, "1 giờ trước"));

        // Set up RecyclerView
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        PostsAdapter adapter = new PostsAdapter(mockPosts);
        recyclerViewPosts.setAdapter(adapter);  // Set Adapter only once

        // Button add new post
        btnPost.setOnClickListener(v -> {
            // Simulate adding a new post
            Toast.makeText(getContext(), "Đang đăng bài...", Toast.LENGTH_SHORT).show();
            // Có thể update
        });

        return rootView;
    }
}
