package me.caelumterrae.fbunewsapp;

import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import me.caelumterrae.fbunewsapp.model.Post;

public class MainActivity extends AppCompatActivity {

    ArrayList<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateMockData();
    }

    private void populateMockData() {
        // C = conservative, L = liberal, M = moderate
        Post p1 = new Post();
        p1.setTitle("C: My Amazing Title");
        p1.setBody("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incidentokof faa STOP | everything beyond STOP should not be displayed inside the post quick blerb (STOP is at 100 chars)");
        p1.setImageUrl("https://pbs.twimg.com/profile_images/962170088941019136/lgpCD8X4_400x400.jpg");
        p1.setPoliticalBias(90);
        // p2 has no media
        Post p2 = new Post();
        p2.setTitle("L: HAS NO MEDIA, HAS 2 RELATED POSTS");
        p2.setBody("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incidentokof faa STOP | everything beyond STOP should not be displayed inside the post quick blerb (STOP is at 100 chars)");
        p1.setPoliticalBias(10);
        // p2 has no media
        Post p3 = new Post();
        p3.setTitle("L: SUPER LONG TITLE & BODY. SUPER LONG TITLE. SUPER LONG TITLE. SUPER LONG TITLE. SUPER LONG TITLE. SUPER LONG TITLE. SUPER LONG TITLE STOP");
        p3.setBody("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Id velit ut tortor pretium viverra suspendisse potenti nullam. Pretium lectus quam id leo in. Netus et malesuada fames ac turpis egestas sed. Tortor pretium viverra suspendisse potenti nullam ac tortor vitae purus. Sagittis id consectetur purus ut. Sagittis vitae et leo duis ut diam quam nulla porttitor. Nibh nisl condimentum id venenatis. Elit sed vulputate mi sit amet mauris commodo quis. Dolor sit amet consectetur adipiscing elit pellentesque habitant. Non nisi est sit amet facilisis magna etiam. Egestas sed tempus urna et pharetra pharetra massa massa. Velit sed ullamcorper morbi tincidunt ornare massa eget egestas. Pulvinar sapien et ligula ullamcorper malesuada proin. Et malesuada fames ac turpis egestas sed tempus urna et. Est velit egestas dui id ornare arcu odio. Arcu risus quis varius quam quisque id diam.\n" +
                "\n" +
                "Viverra vitae congue eu consequat ac felis donec et odio. Bibendum arcu vitae elementum curabitur vitae nunc. Risus in hendrerit gravida rutrum quisque non tellus. Euismod in pellentesque massa placerat. Sit amet facilisis magna etiam tempor. Faucibus pulvinar elementum integer enim neque volutpat ac tincidunt. Enim neque volutpat ac tincidunt vitae semper quis lectus. Morbi tristique senectus et netus et malesuada fames ac. Enim ut sem viverra aliquet. Pellentesque habitant morbi tristique senectus et netus et malesuada. Tellus mauris a diam maecenas sed enim ut sem. Phasellus faucibus scelerisque eleifend donec pretium vulputate sapien nec sagittis. Magna eget est lorem ipsum dolor sit amet consectetur. Vitae proin sagittis nisl rhoncus mattis rhoncus urna neque. Velit scelerisque in dictum non consectetur a erat. Eget magna fermentum iaculis eu non diam phasellus vestibulum.\n" +
                "\n" +
                "Eu non diam phasellus vestibulum lorem. Nisi scelerisque eu ultrices vitae auctor eu augue. Dui nunc mattis enim ut tellus elementum sagittis vitae. Urna cursus eget nunc scelerisque viverra mauris in aliquam. Sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus. Faucibus turpis in eu mi bibendum neque egestas congue. Tellus in metus vulputate eu scelerisque felis imperdiet proin fermentum. Velit scelerisque in dictum non. Tortor condimentum lacinia quis vel. Sed ullamcorper morbi tincidunt ornare. Eget felis eget nunc lobortis mattis aliquam faucibus. Cursus metus aliquam eleifend mi in nulla posuere sollicitudin.\n" +
                "\n" +
                "Potenti nullam ac tortor vitae purus faucibus ornare suspendisse sed. Est lorem ipsum dolor sit amet consectetur. Facilisis volutpat est velit egestas dui id ornare arcu odio. A diam sollicitudin tempor id eu nisl nunc. Enim neque volutpat ac tincidunt vitae semper. Ut lectus arcu bibendum at varius vel pharetra vel. Sem et tortor consequat id porta. Venenatis tellus in metus vulputate eu scelerisque. Ac feugiat sed lectus vestibulum mattis ullamcorper velit sed ullamcorper. Laoreet id donec ultrices tincidunt arcu non sodales. Proin sagittis nisl rhoncus mattis. Vivamus at augue eget arcu dictum varius duis at. Faucibus ornare suspendisse sed nisi. A erat nam at lectus urna. Vitae suscipit tellus mauris a diam. Malesuada pellentesque elit eget gravida cum sociis. Turpis nunc eget lorem dolor sed viverra ipsum nunc aliquet. Vestibulum lorem sed risus ultricies tristique nulla aliquet.\n" +
                "\n" +
                "Morbi non arcu risus quis varius quam. Lectus magna fringilla urna porttitor rhoncus dolor purus non enim. Sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus. Ornare aenean euismod elementum nisi quis. Nibh ipsum consequat nisl vel pretium lectus quam id leo. Mauris commodo quis imperdiet massa tincidunt nunc. Nunc mi ipsum faucibus vitae aliquet nec ullamcorper sit amet. Nisl purus in mollis nunc sed id semper risus in. Tempor nec feugiat nisl pretium fusce id velit ut. Sit amet volutpat consequat mauris. Feugiat vivamus at augue eget arcu dictum varius duis. Quam lacus suspendisse faucibus interdum posuere lorem ipsum dolor sit. Sed viverra ipsum nunc aliquet bibendum enim facilisis. Neque ornare aenean euismod elementum nisi quis eleifend quam. Ut morbi tincidunt augue interdum velit. Id velit ut tortor pretium viverra suspendisse.");
        p3.setImageUrl("https://i.pinimg.com/originals/66/12/7a/66127a9bdf5c5f62402331976c0d9f7e.jpg");
        p1.setPoliticalBias(3);

        Post p4 = new Post();
        p4.setTitle("M: HAS NO MEDIA, HAS 4 RELATED POSTS");
        p4.setBody("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incidentokof faa STOP | everything beyond STOP should not be displayed inside the post quick blerb (STOP is at 100 chars)");
        p1.setPoliticalBias(50);

        ArrayList<Post> related_p2 = new ArrayList<>();
        related_p2.add(p2);
        related_p2.add(p3);
        p2.setRelatedPosts(related_p2);

        ArrayList<Post> related_p4 = new ArrayList<>();
        related_p4.add(p1);
        related_p4.add(p2);
        related_p4.add(p3);
        related_p4.add(p4);
        p4.setRelatedPosts(related_p4);

        posts.add(p1);
        posts.add(p2);
        posts.add(p3);
        posts.add(p4);
    }
}
