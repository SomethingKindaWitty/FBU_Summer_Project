package me.caelumterrae.fbunewsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import me.caelumterrae.fbunewsapp.client.TopNewsClient;
import me.caelumterrae.fbunewsapp.model.Post;

public class MainActivity extends AppCompatActivity {

    ArrayList<Post> posts;
    RecyclerView rvPosts;
    FeedAdapter feedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find the RecyclerView
        rvPosts = (RecyclerView) findViewById(R.id.rvPost);
        //init the ArrayList (data source)
        posts = new ArrayList<>();
        //construct adapter from data source
        feedAdapter = new FeedAdapter(posts);
        //RecyclerView setup (layout manager, use adapter)
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        //set adapter
        rvPosts.setAdapter(feedAdapter);

        //populateMockData();

        TopNewsClient client = new TopNewsClient();
        client.getTopNews(feedAdapter, posts);
    }

    private void populateMockData() {
        // C = conservative, L = liberal, M = moderate
        Post p1 = new Post();
        p1.setTitle("C: On Amazon Prime Day, site and app crash; shoppers left in lurch instead see photos of dogs");
        p1.setSummary("Amazon launched its fourth annual Prime Day sale at noon west coast time on Monday. The site instantly crashed.");
        p1.setBody("SAN FRANCISCO – Amazon suffered a rare glitch at the start of its 36-hour online sales bonanza Monday, turning away would-be shoppers browsing for Prime Day deals with notices that heavy web traffic had caused its site and app to crash. Instead, the online retail giant, which also owns cloud computing juggernaut Amazon Web Services, showed shoppers a selection of dog photos. Last year’s Prime Day sale was estimated to have generated about $2.4 billion in sales, and this year’s 36-hour sale was predicted to reach $3.4 billion, according to analysis by Coresight Research. In a statement, Amazon said that some customers were having difficulty shopping and it was quickly working to resolve the issue. Amazon was determined to make sure Americans knew it was Prime Day, and the effort seemed to have paid off with a flood of visitors who appeared to have overwhelmed the company's systems. The best Amazon Prime Day deals right now The company spent $3.1 million on its Prime Day awareness ad on television this year, an analysis by Market Track, a Chicago-based ad and pricing analysis firm, found. After its fourth annual Prime Day sale started at noon West Coast time Monday, it was not possible to make purchases in the first hour of the sale. Users could access their online shopping cart, but when they attempted to actually purchase something, they got a note saying, Sorry, we're experiencing unusually heavy traffic. Please try again in a few seconds. Your items are still waiting in your cart. The much-vaunted Amazon deals were also not available during the outage. When users attempted to click on the Shop Deals b");
        p1.setImageUrl("https://www.gannett-cdn.com/-mm-/93ea590c7bf4b87e3bea6b9bc30718901a7216de/c=197-347-500-518/local/-/media/2018/07/16/USATODAY/USATODAY/636673519041885107-IMG-8669.png?width=3200&height=1680&fit=crop");
        p1.setPoliticalBias(90);
        p1.setDate("2018-07-16T21:09:41Z");
        p1.setUrl("https://www.usatoday.com/story/tech/2018/07/16/amazon-launched-its-huge-prime-day-sale-and-its-site-instantly-crashed/789631002/");
        // p2 has no media
        Post p2 = new Post();
        p2.setTitle("L: HAS NO MEDIA, No summary, HAS 2 RELATED POSTS");
        p2.setBody("NEW YORK (AP) — Major U.S. indexes closed mostly lower Monday as investors bought banks but sold most other types of stocks, including health care and technology companies. Energy stocks sank along with oil prices. Oil prices fell more than 4 percent after U.S. officials suggested the U.S. will take a softer stance on countries that import oil from Iran after sanctions on Iran's energy sector go back into effect in November. Banks rose along with interest rates as well as a solid second-quarter report from Bank of America. A strong forecast gave Deutsche Bank its biggest gain in more than a year. Amazon jumped in midday trading as investors expected strong sales during the company's annual Prime Day promotion, one of its largest sales days of the year, but the stock gave up much of that gain following problems with the company's website. Most other groups of stocks lost ground, and about two-thirds of the companies on the New York Stock Exchange finished lower. Stocks finished at five-month highs Friday as investors remained optimistic about the U.S. economy even as they worried about the trade war between the U.S. and China. We're coming off of a very strong week last week where the market finally started to focus on the expectation of a very strong earnings season, said Sunitha Thomas, a portfolio advisor for Northern Trust Wealth Management. She said companies are likely to report big increases in profit and revenue, and while investors are looking for hints the trade war is affecting company forecasts and supply chains, there were no signs of that on Monday. The S&P 500 index lost 2.88 points, or 0.1 percent, to 2,798.43. The Dow Jones Industrial Average rose 44.95 points, or 0.2 percent, to 25,064.36 as Goldman Sachs, JPMorgan Chase, and Boeing climbed. The Nasdaq composite fell 20.26 points, or 0.3 percent, to 7,805.72. The Russell 2000 index of smaller-company stocks declined 8.54 points, or 0.5 percent, to 1,678.54. Bank of America's second-quarter profits jumped, as like other big banks, it got a big boost from the corporate tax cut at the end of 2017 and from higher interest rates. Unlike Wells Fargo and Citigroup, which disclosed their results Friday, Bank of America did better than Wall Street expected. Its stock rose 4.3 percent to $29.78. Deutsche Bank jumped 8 percent to $12.14 after it said its earnings will be considerably higher than analysts expected. Deutsche Bank stock has tumbled as the company has taken three years of losses based on high costs and big fines and penalties linked to past misconduct. Benchmark U.S. crude fell 4.2 percent to $68.06 in New York. Brent crude, used to price international oils, fell 4.6 percent to $71.84 a barrel in London.");
        p2.setPoliticalBias(10);
        p2.setSummary("As Prime Day catches on — and brings in billions — workers are finding ways to hit Amazon where it hurts.");
        p2.setDate("2018-07-16T20:42:52Z");
        p2.setUrl("https://www.usnews.com/news/business/articles/2018-07-16/asian-shares-drift-lower-as-china-data-trade-cast-shadows");
        // p2 has no media
        Post p3 = new Post();
        p3.setTitle("L: SUPER LONG TITLE & BODY. SUPER LONG TITLE. SUPER LONG TITLE. SUPER LONG TITLE. SUPER LONG TITLE. SUPER LONG TITLE. SUPER LONG TITLE Ayyyyyyyyyyyyyyyyyyyy");
        p3.setSummary("As Prime Day catches on — and brings in billions — workers are finding ways to hit Amazon where it hurts.");
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
        p3.setPoliticalBias(3);
        p3.setDate("2018-07-16T18:46:05Z");
        p3.setUrl("https://www.washingtonpost.com/business/2018/07/16/amazon-prime-day-now-an-opportunity-worker-strikes-consumer-protests-around-world/");

        Post p4 = new Post();
        p4.setTitle("M: HAS NO MEDIA, HAS 4 RELATED POSTS");
        p4.setSummary("Morgan Stanley's stock market analyst warns clients to stop yawning at his bearish call and get defensive");
        p4.setBody("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incidentokof faa STOP | everything beyond STOP should not be displayed inside the post quick blerb (STOP is at 100 chars)");
        p4.setPoliticalBias(50);
        p4.setDate("2018-07-16T18:16:07Z");
        p4.setUrl("https://www.cnbc.com/2018/07/16/morgan-stanley-strategist-stop-yawning-at-bearish-earnings-call.html");

        ArrayList<Post> related_p2 = new ArrayList<>();
        related_p2.add(p1);
        related_p2.add(p3);
        p2.setRelatedPosts(related_p2);
        p1.setRelatedPosts(related_p2);

        ArrayList<Post> related_p4 = new ArrayList<>();
        related_p4.add(p1);
        related_p4.add(p2);
        related_p4.add(p3);
        related_p4.add(p4);
        p4.setRelatedPosts(related_p4);
        p3.setRelatedPosts(related_p4);

        posts.add(p1);
        posts.add(p2);
        posts.add(p3);
        posts.add(p4);
    }
}
