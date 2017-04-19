package com.shivang.dailyeconomy.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shivang.dailyeconomy.R;
import com.shivang.dailyeconomy.fragment.SectionFragment;
import com.shivang.dailyeconomy.misc.StudentModel;
import com.shivang.dailyeconomy.misc.UserLocalStore;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kshivang on 07/12/16.
 *
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private TextView profileTId, profileName;
    private CircleImageView profileImage;
    private ViewPager mViewPager;
    private UserLocalStore userLocalStore;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_home);

        mAuth = FirebaseAuth.getInstance();
        userLocalStore = new UserLocalStore(MainActivity.this);
        onClassSection(userLocalStore.getUid());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setTitle("Home");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);

            NavigationView headerView = (NavigationView) navigationView
                    .inflateHeaderView(R.layout.nav_header_home);
            if (headerView != null) {
                profileImage = (CircleImageView) headerView.findViewById(R.id.img_profile);
                profileName = (TextView) headerView.findViewById(R.id.text_name);
                profileTId = (TextView) headerView.findViewById(R.id.tid);

                headerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        drawer.closeDrawer(GravityCompat.START);
//                        startActivity(new Intent(MainActivity.this, ProfileScreen.class));
                    }
                });
            }
        }

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.primary_tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);

        final String[] sections = getResources().getStringArray(R.array.section);

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position < sections.length) return SectionFragment.newInstance(sections[position]);
                return null;
            }

            @Override
            public int getCount() {
                return sections.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (sections[position] != null) return sections[position];
                return null;
            }
        };

        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        drawer.closeDrawer(GravityCompat.START);
        navigationView.setCheckedItem(R.id.nav_header_home);

        switch (item.getItemId()) {
            case R.id.home:
                mViewPager.setCurrentItem(0, true);
//                startActivity(new Intent(MainActivity.this, ProfileScreen.class));
//                break;
//            case R.id.profile:
//                startActivity(new Intent(HomeScreen.this, SchoolInfoScreen.class));
//                break;
            case R.id.changePassword:
//                startActivity(new Intent(MainActivity.this, ChangePasswordScreen.class));
                break;
            case R.id.help:
//                startActivity(new Intent(MainActivity.this, HelpScreen.class));
                break;
            case R.id.logout:
                finish();
                break;
        }
       return true;
    }

    private void onClassSection(String uid){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("students").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot != null){
                            StudentModel studentModel = dataSnapshot
                                    .getValue(StudentModel.class);
                            if (studentModel != null) {
                                userLocalStore.setStudentModel(studentModel);
                                updateHeader(studentModel);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void updateHeader(StudentModel studentModel){
        if(studentModel == null) return;
        profileTId.setText(String.format(Locale.ENGLISH,"%d", studentModel.getRoll()));
        profileName.setText(String.format("%s", studentModel.getName()));
        Picasso.with(MainActivity.this).load(studentModel.getImage_url())
                .error(R.drawable.placeholder_profile)
                .placeholder(R.drawable.placeholder_profile)
                .into(profileImage);
    }
}
