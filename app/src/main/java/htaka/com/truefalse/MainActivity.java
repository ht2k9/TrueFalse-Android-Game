package htaka.com.truefalse;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.unity3d.ads.UnityAds;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    Techniques[] techniques=new Techniques[37];

    List<Facts> facts = new ArrayList<Facts>();
    List<Integer> answeredQues = new ArrayList<Integer>();
    List<Integer> unansweredQues = new ArrayList<Integer>();

    TextView text1, text2, scoreTxt;
    ImageView img1,img2;

    int score = 0, counter = 0, randNum , noloop=0;

    Random rand = new Random();

    private Intent intent;

    MediaPlayer mpC,mpF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        intent = new Intent(MainActivity.this,MenuActivity.class);

        noloop = getIntent().getIntExtra("Loop",noloop);

        SetTechniques();
        SetViews();
        InsertAllQuestions();
        SetQuestion();
        setupWindowAnimations();
    }

    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide();
            slide.setDuration(1000);
            getWindow().setExitTransition(slide);
        }
    }

    private void SetTechniques() {
        techniques[0] = Techniques.Flash;
        techniques[1] = Techniques.Pulse;
        techniques[2] = Techniques.RubberBand;
        techniques[3] = Techniques.Shake;
        techniques[4] = Techniques.Swing;
        techniques[5] = Techniques.Wobble;
        techniques[6] = Techniques.Bounce;
        techniques[7] = Techniques.Tada;
        techniques[8] = Techniques.StandUp;
        techniques[9] = Techniques.Wave;
        techniques[10] = Techniques.ZoomInUp;
        techniques[11] = Techniques.RollIn;
        techniques[12] = Techniques.Landing;
        techniques[13] = Techniques.DropOut;
        techniques[14] = Techniques.BounceIn;
        techniques[15] = Techniques.BounceInUp;
        techniques[16] = Techniques.BounceInDown;
        techniques[17] = Techniques.BounceInLeft;
        techniques[18] = Techniques.BounceInRight;
        techniques[19] = Techniques.FadeIn;
        techniques[20] = Techniques.FadeInUp;
        techniques[21] = Techniques.FadeInDown;
        techniques[22] = Techniques.FadeInLeft;
        techniques[23] = Techniques.FadeInRight;
        techniques[24] = Techniques.FlipInX;
        techniques[25] = Techniques.RotateIn;
        techniques[26] = Techniques.RotateInUpLeft;
        techniques[27] = Techniques.RotateInUpRight;
        techniques[28] = Techniques.RotateInDownLeft;
        techniques[29] = Techniques.RotateInDownRight;
        techniques[30] = Techniques.SlideInLeft;
        techniques[31] = Techniques.SlideInRight;
        techniques[32] = Techniques.SlideInUp;
        techniques[33] = Techniques.SlideInDown;
        techniques[34] = Techniques.ZoomInRight;
        techniques[35] = Techniques.ZoomInDown;
        techniques[36] = Techniques.ZoomInLeft;
    }

    private void SetViews() {
        text1 = (TextView) findViewById(R.id.textView1);
        text2 = (TextView) findViewById(R.id.textView2);
        scoreTxt = (TextView) findViewById(R.id.scoreTextView);

        img1 = (ImageView) findViewById(R.id.imageView1);
        img2 = (ImageView) findViewById(R.id.imageView2);

        mpC = MediaPlayer.create(this, R.raw.corranswer);
      //  mpF = MediaPlayer.create(this, R.raw.sound);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnswerQuestion(1);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnswerQuestion(2);
            }
        });
    }

    private void InsertAllQuestions() {
        facts.add(new Facts("عمر السلحفاة يصل الى ١٥٠ سنة","عمر السلحفاة يصل الى ٢٠ سنة",1 , "١٥٠" , "٢٠"));
        facts.add(new Facts("الدلفين هو أذكى الحيوانات","الثعلب هو أذكى الحيوانات",1 , "الدلفين" , "الثعلب"));
        facts.add(new Facts("لون الكوكاكولا في الأصل كان أحمر","لون الكوكاكولا في الأصل كان أخضر",2  , "أحمر" , "أخضر"));
        facts.add(new Facts("يمنع في اوهايو إمساك القطط دون رخصة صيد","يمنع في اوهايو إمساك الفئران دون رخصة صيد", 2 , "القطط" , "الفئران"));
        facts.add(new Facts("مستخدم اليد اليمنى يعيش أطول من مستخدم اليسرى","مستخدم اليد اليسرى يعيش أطول من مستخدم اليمنى", 1 , "اليمنى" , "اليسرى"));
        facts.add(new Facts("لا يستطيع الغزال البلع إذا كان مقلوبا","لا يستطيع الدجاج البلع إذا كان مقلوباً", 2 , "الغزال" , "الدجاج"));
        facts.add(new Facts("تسقط النملة على جانبها الأيسر عندما تتسمّم","تسقط النملة على جانبها الأيمن عندما تتسمّم", 2 , "الأيسر" , "الأيمن"));
        facts.add(new Facts("الحمار يغرق في الرّمال المتحرّكة لكن البغل ينجو منها","البغل يغرق في الرّمال المتحرّكة لكن الحمار ينجو منها", 1 , "الحمار" , "البغل"));
        facts.add(new Facts("يعتبر اللوز نوعاً من أنواع الخوخ","يعتبر اللوز نوعاً من أنواع الاجاص", 1 , "الخوخ" , "الاجاص"));
        facts.add(new Facts("ينبض القلب 10 ألف مرّة في اليوم","ينبض القلب 100 ألف مرّة في اليوم", 2 , "10" , "100"));
        facts.add(new Facts("تقتل القردة سنويّاً اناساً أكثر من حوادث الطائرات","تقتل حوادث الطائرات سنويّاً اناساً أكثر من القردة", 1 , "القردة" , "حوادث الطائرات"));
        facts.add(new Facts("ثلثي عمليّات الاختطاف في العالم تتمّ في النرويج","ثلثي عمليّات الاختطاف في العالم تتمّ في كولومبيا", 2 , "النرويج" , "كولومبيا"));
        facts.add(new Facts("أطول زمن لطيران النعامة هو 13 ثانية","أطول زمن لطيران الدجاجة هو 13 ثانية", 2 , "النعامة" , "الدجاجة"));
        facts.add(new Facts("مقابل كل صفحة عادية على الإنترنت توجد حوالي 5 صفحات إباحية","مقابل كل صفحة إباحية على الإنترنت توجد حوالي 5 صفحات عادية",1 , "عادية" , "إباحية"));
        facts.add(new Facts("يبلغ معدّل عمر اليعسوب 24 يوم ","يبلغ معدّل عمر اليعسوب 24 ساعة ", 2 , "يوم" , "ساعة"));
        facts.add(new Facts("كندا هي كلمة هندية الأصل، وتعني القرية الكبيرة", "كندا هي كلمة سنسكريتية الأصل، وتعني القرية الكبيرة", 1 , "هندية" , "سنسكريتية"));
        facts.add(new Facts("الشخص الذي يحب النوم أذكى من الذي يحب السهر","الشخص الذي يحب السهر أذكى من الذي يحب النوم", 2 , "النوم" , "السهر"));
        facts.add(new Facts("في حالة العطس والعينان مفتوحتان، فإن العينان ستنفجر","في حالة العطس والعينان مفتوحتان، فإن العينان ستعمى", 1 , "ستنفجر" , "ستعمى"));
        facts.add(new Facts("الرجل اخترع السترة الواقية من الرصاص","المرأة اخترعت السترة الواقية من الرصاص", 2 , "الرجل", "المرأة"));
        facts.add(new Facts("بعض من يشاهدون برنامج سبونج بوب ينسون بأنهم تحت الماء","بعض من يشاهدون برنامج سبونج بوب يظنون بأنهم تحت الماء", 1 , "ينسون", "يظنون"));
        facts.add(new Facts("الجو الحار يزيد من احتمال رؤية الكوابيس","الجو البارد يزيد من احتمال رؤية الكوابيس", 2 , "الحار", "البارد"));
        facts.add(new Facts("لون الحبر الأزرق يساعد على التذكر","لون الحبر الأصفر يساعد على التذكر", 1 , "الأزرق", "الأصفر"));
        facts.add(new Facts("القلب هو السبب الأساسي في نسيان خطأ شخص معين ومسامحته","الدماغ هو السبب الأساسي في نسيان خطأ شخص معين ومسامحته", 2 , "القلب", "الدماغ"));
        facts.add(new Facts("أسرع الطرق لانتشار موضوع هو المرأة","أسرع الطرق لانتشار موضوع هو الايميل", 1 , "المرأة", "الايميل"));//25
        facts.add(new Facts("إن الشك يصيب كل الناس عدا الأذكياء","إن الشك يصيب كل الناس عدا الأغبياء", 2 , "الأذكياء", "الأغبياء"));
        facts.add(new Facts("تظل المرأة تتكلم حتى تبكي","تظل المرأة تتكلم حتى تضحك", 1 , "تبكي", "تضحك"));
        facts.add(new Facts("آخر ما يموت في الرجل لسانه، وآخر ما يموت في المرأة قلبه","آخر ما يموت في الرجل قلبه، وآخر ما يموت في المرأة لسانها", 2 , "لسانه", "قلبه"));
        facts.add(new Facts("أكثر ما يجعل الفتاة تغضب هو أن تجعلها دون مرآة يوماً كاملاً","أكثر ما يجعل الفتاة تغضب هو أن تجعلها دون مكياج يوماً كاملاً", 1 , "مرآة", "مكياج"));
        facts.add(new Facts("قلب الإنسان يميل إلى الجهة اليمنى من الجسم","قلب الإنسان يميل إلى الجهة اليسرى من الجسم", 2 , "اليمنى", "اليسرى"));
        facts.add(new Facts("نزول أول دمعة من العين اليمنى يكون سببها فرحة وسعادة","نزول أول دمعة من العين اليمنى يكون سببها حزن وألم", 1 , "فرحة وسعادة", "حزن وألم"));
        facts.add(new Facts("الأصم لا يستخدم أذنه لكي يفكر بالأصوات المسموعة من حوله","الأصم لا يستخدم عقله لكي يفكر بالأصوات المسموعة من حوله", 2 , "أذنه", "عقله"));
        facts.add(new Facts("الإنسان عند أول 3 ثوانٍ يستيقظ فيها من النوم يكون فاقداً لذاكرته","الإنسان عند أول 30 ثوانٍ يستيقظ فيها من النوم يكون فاقداً لذاكرته", 1 , "3", "30"));
        facts.add(new Facts("الأعمى لا يرى شيئا ما حوله","الأعمى يرى ما حوله باللون الأسود", 1 , "لا", "باللون الأسود"));
        facts.add(new Facts("أطول شجرة في العالم توجد في كاليفورنيا ويبلغ طولها 367 قدماً", "أطول شجرة في العالم توجد في كاليفورنيا ويبلغ طولها 367 متر", 1 , "قدماً", "متر"));
        facts.add(new Facts(" مشاهدة التلفزيون بالأسود تؤدي إلى السمنة المفرطة", " مشاهدة التلفزيون الملون تؤدي إلى السمنة المفرطة", 2 , "بالأسود", "الملون"));
        facts.add(new Facts(" في جسم الانسان من الخلايا ما يقدر بنحو 32 بليون خلية", " في جسم الانسان من الخلايا ما يقدر بنحو 32 مليون خلية", 1 , "بليون", "مليون"));
        facts.add(new Facts("اليدان تشتملان على ٣٩ عظمة","اليدان تشتملان على ٢٧ عظمة", 2 , "٣٩", "٢٧"));
        facts.add(new Facts("الماء الساخن أثقل من الماء البارد", "الماء البارد أثقل من الماء الساخن", 1 , "الساخن", "البارد"));
        facts.add(new Facts("النوتيلا كان نادر في ايطاليا لذلك صنع الكاكاو بدل عنه","الكاكاو كان نادر في ايطاليا لذلك صنع النوتيلا بدل عنه", 2 , "الكاكاو", "النوتيلا"));
        facts.add(new Facts("كليوباترا عاشت في زمن أقرب لبناء بيتزا هت من بناء الأهرامات","كليوباترا عاشت في زمن أقرب لبناء الأهرامات من بناء بيتزا هت", 1 , "بيتزا هت", "الأهرامات"));
        facts.add(new Facts("عمر حضارة الأزتيك أكبر من عمر أوكسفورد","عمر أوكسفورد أكبر من عمر حضارة الأزتيك", 2 , "الأزتيك", "أوكسفورد"));
        facts.add(new Facts("في فترة 66 عاماً انتقلنا من الطيران إلى الهبوط على القمر","في فترة 66 شهرا انتقلنا من الطيران إلى الهبوط على القمر", 1 , "عاماً", "شهرا"));
        facts.add(new Facts("صنعت اكبر قطعة من الكعكة من ما يعادل 10,000 قطعة من العلكة","صنعت اكبر قطعة من العلكة من ما يعادل 10,000 قطعة من العلكة", 2 , "الكعكة", "العلكة"));
        facts.add(new Facts("يقال إن مضغ العلكة يمنع البكاء","مثبوت علميا مضغ العلكة يمنع البكاء", 1 , "يقال إن", "مثبوت علميا"));
        facts.add(new Facts("مضغ العلكة بعد تناول الطعام يساعد في صنع الحرقة","مضغ العلكة بعد تناول الطعام يساعد في منع الحرقة ", 2 , "صنع", "منع"));
        facts.add(new Facts("ضرب الرأس بالجدار يحرق ١٥٠ سعرة حرارية بالساعة","ضرب الاصبع بمطرقة يحرق ١٥٠ سعرة حرارية بالساعة", 1 , "الرأس بالجدار", "الاصبع بمطرقة"));
        facts.add(new Facts("اضطراب إدمان الفيسبوك : هو مرض أطلقه عامة الناس","اضطراب إدمان الفيسبوك : هو اضطراب عقلي حدده علماء النفس", 2 , "هو مرض أطلقه عامة الناس", "هو اضطراب عقلي حدده علماء النفس"));
        facts.add(new Facts("عندما تكون أفراس النهر غاضبة، عرقهم يصبح أحمر","عندما تكون أفراس النهر فرحة، عرقهم يصبح أحمر", 1 , "غاضبة", "فرحة"));
        facts.add(new Facts("الأرنب لا يمكنه القفز اذا رفعت ذيله عن الأرض","الكنغر لا يمكنه القفز اذا رفعت ذيله عن الأرض", 2 , "الأرنب", "الكنغر"));//50
        facts.add(new Facts("من المرجح حدوث النوبات القلبية يوم الاثنين","من المرجح حدوث نوبات الغضب يوم الاثنين", 1 , "النوبات القلبية", "نوبات الغضب"));
        facts.add(new Facts("حجم الأخطبوط الطفل عند الولادة مثل حجم الفيل","حجم الأخطبوط الطفل عند الولادة مثل حجم البرغوث", 2 , "الفيل", "البرغوث"));
        facts.add(new Facts("الفيسبوك، سكايب وتويتر محظورة في الصين", "الافلام الكوميدية محظورة في الصين", 1 , "الفيسبوك، سكايب وتويتر", "الافلام الكوميدية"));
        facts.add(new Facts("المرأة العربية يمكن أن تطلب الطلاق إذا صب أزواجهن القهوة لهم.","المرأة العربية يمكن أن تطلب الطلاق إذا لم يصب أزواجهن القهوة لهم.", 2 , "صب", " لم يصب"));
        facts.add(new Facts("صاعقة البرق ست مرات أكثر سخونة من الشمس"," الشمس ست مرات أكثر سخونة من صاعقة البرق", 1 , "صاعقة البرق", "الشمس"));
        facts.add(new Facts("الكلاب تنام 16 إلى 18 ساعة في اليوم","القطط تنام 16 إلى 18 ساعة في اليوم", 2 , "الكلاب", "القطط"));
        facts.add(new Facts("الاسم الأكثر شيوعا في العالم هو محمد","الاسم الأكثر شيوعا عند العرب هو محمد", 1 , "في العالم", "عند العرب"));
        facts.add(new Facts("عندما تموت يسقط شعرك بعد بضعة أشهر","عندما تموت يبقى شعرك ينمو لبضعة أشهر", 2 , "يسقط", "يبقى"));
        facts.add(new Facts("أكبر مقدار المال دفع ثمن لبقرة كان 1.3 مليون دولار","أكبر مقدار المال دفع ثمن لدراجة هوائية كان 1.3 مليون دولار", 1 , "لبقرة", "لدراجة هوائية"));
        facts.add(new Facts("قلب الحوت الأزرق كبير لدرجة انه بإمكان فيل ان يسبح بداخله","قلب الحوت الأزرق كبير لدرجة انه بإمكان طفل ان يسبح بداخله", 2 , "فيل", "طفل"));
        facts.add(new Facts("عدد البحيرات في كندا يفوق عدد بحيرات دول العالم مجتمعة", "عدد البحيرات في دول العالم يفوق عدد بحيرات كندا مجتمعة", 1 , "كندا", "دول العالم"));
        facts.add(new Facts("معدل القراءة عند القارئ العربي هو 4 صفحات سنويا", "معدل القراءة عند القارئ العربي هو 8 صفحات سنويا", 1 , "4", "8"));
        facts.add(new Facts("اغلب الاشخاص لا يعرفون كيف يتكلمون إذا كانوا ينظرون إلى شخص معجبين به", "اغلب الاشخاص لا يعرفون كيف يتكلمون إذا كانوا ينظرون إلى شخص يكرهونه", 1 , "معجبين به", "يكرهونه"));
        facts.add(new Facts("إجهاد نفسك في التمارين قد يسبب لك نحافة الوزن", "إجهاد نفسك في التمارين قد يسبب لك زيادة الوزن", 2 , "نحافة", "زيادة"));
        facts.add(new Facts("الاستماع إلى تعليقات الناس الساخرة اليومية عليك تزيد من ابداعك", "الاستماع إلى تعليقات الناس الساخرة اليومية عليك تقلل من ابداعك", 1 , "تزيد", "تقلل"));
        facts.add(new Facts("الكوكا كولا يحتوي على مادة تساعد على الضحك", "الموز يحتوي على مادة تساعد على الضحك", 2 , "الكوكا كولا", "الموز"));
        facts.add(new Facts("الطعام الوحيد الذي لا يفسد هو العسل", "الطعام الوحيد الذي لا يفسد هو التمر", 1 , "العسل", "التمر"));
        facts.add(new Facts("التمساح لا يستطيع أن يدخل لسانه", " التمساح لا يستطيع أن يخرج لسانه", 2 , "يدخل", "يخرج"));
        facts.add(new Facts("بدنيا من المستحيل ان ينظر الخنزير إلى السماء", "نفسيا من المستحيل ان ينظر الخنزير إلى السماء", 1 , "بدنيا", "نفسيا"));
        facts.add(new Facts(" صوت البطة لا يرد الصدى في أي مكان والسبب معروف", " صوت البطة لا يرد الصدى في أي مكان والسبب غير معروف", 2 , "معروف", "غير معروف"));
        facts.add(new Facts("يستطيع الرجل قراءة الحروف الصغيرة اكثر من المرأة لكن المرأة سمعها أقوى", "يستطيع الرجل قراءة الحروف الصغيرة اكثر من المرأة لكن المرأة لمستها ناعمة", 1 , "سمعها أقوى", "لمستها ناعمة"));
        facts.add(new Facts("اخر مالك لشركة مالبورو للسجائر مات مصاباً بسرطان الرئة", "أول مالك لشركة مالبورو للسجائر مات مصابا بسرطان الرئة", 2 , "اخر", "أول"));
        facts.add(new Facts("معظم الأغبرة داخل المنازل تكون نتيجة بقايا الجلد الميت الذي يسقط من سكانها", "معظم الأغبرة داخل المنازل تكون نتيجة بقايا الأوساخ التي يسقط من سكانها", 1 , "الجلد الميت", "الأوساخ"));
        facts.add(new Facts("الكلب هو المخلوق الوحيد الذي ينام على ظهره", "الإنسان هو المخلوق الوحيد الذي ينام على ظهره", 2 , "الكلب", "الإنسان"));
        facts.add(new Facts("حبات اللؤلؤ تذوب في الخل", "الجواهر تذوب في الخل", 1 , "حبات اللؤلؤ", "الجواهر"));
        facts.add(new Facts("قليل رؤساء الولايات المتحدة الأمريكية كانوا يرتدون النظارات", "جميع رؤساء الولايات المتحدة الأمريكية كانوا يرتدون النظارات", 2 , "قليل", "جميع"));
        facts.add(new Facts(" السعرات الحرارية التي تحرق أثناء النوم تفوق التي يحرقها أثناء مشاهدته للتلفاز", " السعرات الحرارية التي تحرق أثناء مشاهدة للتلفاز تفوق التي يحرقها أثناء النوم", 1 , "النوم", "مشاهدة للتلفاز"));
        facts.add(new Facts("كوكب الأرض هو الكوكب الوحيد الذي يدور مع حركة عقارب الساعة", "كوكب الزهرة هو الكوكب الوحيد الذي يدور مع حركة عقارب الساعة", 2 , "الأرض", "الزهرة"));
        facts.add(new Facts("هناك 6 أشخاص يشبهونك في العالم", "لا يوجد أشخاص يشبهونك في العالم", 1 , "هناك 6", "لا يوجد"));
        facts.add(new Facts("النوم بدون وضع الرأس على وسادة يزيد من آلام الظهر", "النوم بدون وضع الرأس على وسادة يقلل من آلام الظهر", 2 , "يزيد", "يقلل"));
        facts.add(new Facts("الإنسان يرث من والده الطول ومن والدته الوزن", "الإنسان يرث من والدته الطول ومن والده الوزن", 1 , "والده", "والدته"));
        facts.add(new Facts("لو شعرت بحزن مفاجئ، فقط هز رأسك وسيعود لك النشاط مرة أخرى", "لو شعرت بإجهاد مفاجئ، فقط هز رأسك وسيعود لك النشاط مرة أخرى", 2 , "بحزن", "بإجهاد"));
        facts.add(new Facts("العقل لا يستطيع غض البصر عن (الطعام والأشخاص الجذابين والأخطار)", "العقل لا يستطيع غض البصر عن (الطعام والأشخاص الجذابين والضحك)", 1 , "الأخطار", "الضحك"));
        facts.add(new Facts(" إذا جلس الإنسان في اليوم الواحد أكثر من 11 ساعة فإنه سيتوفى خلال ثلاث سنين", " إذا جلس الإنسان في اليوم الواحد أكثر من 11 ساعة فإنه سيتوفى خلال ثلاث أشهر", 2 , "سنين", "أشهر"));
        facts.add(new Facts("أكياس الشاي الجافة تعمل على أمتصاص الروائح الكريهة وخاصة الموجودة في الأحذية", "أكياس الشاي الجافة تعمل على أمتصاص الروائح الجميلة وخاصة الموجودة في الأحذية", 1 , "الكريهة", "الجميلة"));
        facts.add(new Facts("اينشتاين أكد أنه إذا اختفى الذباب، سيموت الكثير من الأشخاص فجأة خلال 4 سنوات", "اينشتاين أكد أنه إذا اختفى النحل، سيموت الكثير من الأشخاص فجأة خلال 4 سنوات", 2 , "الذباب", "النحل"));
        facts.add(new Facts("يمكن للإنسان أن يعيش بدون طعام لأسابيع ولكن 11 يوم فقط بدون نوم", "يمكن للإنسان أن يعيش بدون نوم لأسابيع ولكن 11 يوم فقط بدون طعام", 1 , "طعام", "نوم"));
        facts.add(new Facts("يتمتع الأشخاص الذي يضحكون كثيراً بجاذبية أفضل من غيرهم", "يتمتع الأشخاص الذي يضحكون كثيراً بصحة أفضل من غيرهم", 2 , "بجاذبية", "بصحة"));
        facts.add(new Facts("يستطيع مخ الإنسان أن يسجل أكثر من 5 أضعاف المسجلة في موسوعة ويكيبيديا", "يستطيع مخ الإنسان أن يسجل أكثر من 10 أضعاف المسجلة في موسوعة ويكيبيديا", 1 , "5", "10"));
        facts.add(new Facts("الكسل وقلة الحركة يؤديان إلى الموت البطيء عكس يفعل التدخين", "الكسل وقلة الحركة يؤديان إلى الموت البطيء مثلما يفعل التدخين", 2 , "عكس", "مثلما"));
        facts.add(new Facts("المخ يستهلك نفس معدل الطاقة الموجود في مصباح كهربائي بقوة 10 واط", "القلب يستهلك نفس معدل الطاقة الموجود في مصباح كهربائي بقوة 10 واط", 1 , "المخ", "القلب"));
        facts.add(new Facts("يفرز جسم النبتة حرارة كل 30 ثانية كافية لغلي لتر ونصف من الماء", "يفرز جسم الإنسان حرارة كل 30 ثانية كافية لغلي لتر ونصف من الماء", 2 , "النبتة", "الإنسان"));
        facts.add(new Facts("في الواقع, التكلم الى نفسك يجعلك أكثر ذكاء", "في الواقع, التكلم الى نفسك يجعلك أكثر غباء", 1 , "ذكاء", "غباء"));
        facts.add(new Facts("دراسة تؤكد أن الأنترنت يجعلنا اجتماعيين, سعداء ومرتاحين", "دراسة تؤكد أن الأنترنت يجعلنا وحيدين, محطمين ومجانين", 2 , "اجتماعيين, سعداء ومرتاحين", "وحيدين, محطمين ومجانين"));
        facts.add(new Facts("الأشخاص الحساسون يميلون إلى أن يكونوا أكثر محبة وتفاهم ومراعاة للأخرين", "الأشخاص الخجلون يميلون إلى أن يكونوا أكثر محبة وتفاهم ومراعاة للأخرين", 1 , "الحساسون", "الخجلون"));
        facts.add(new Facts("قلب السرطان في رأسه", "قلب القريدس في رأسه", 2 , "السرطان", "القريدس"));
        facts.add(new Facts("الفئران والخيول لا يمكن أن تتقيأ", "الفيلة والحمير لا يمكن أن تتقيأ", 1 , "الفئران والخيول", "الفيلة والحمير"));
        facts.add(new Facts("اخترعت أعواد الكبريت قبل ولاعة السجائر", "اخترعت ولاعة السجائر قبل أعواد الكبريت", 2 , "أعواد الكبريت", "ولاعة السجائر"));
        facts.add(new Facts("معظم أصابع أحمر الشفاه تحتوي على قشور السمك", "جميع أصابع أحمر الشفاه تحتوي على قشور السمك", 1 , "معظم", "جميع"));
        facts.add(new Facts("عكس بصمات الأصابع، طبعة اللسان مختلفة", "مثل بصمات الأصابع، طبعة اللسان مختلفة", 2 , "عكس", "مثل"));
        facts.add(new Facts("يسمى الجمل سفينة الصحراء", "يسمى الجمل ملك الصحراء", 1 , "سفينة", "ملك"));//100th question
        facts.add(new Facts("كرة المطاط تقفز أعلى من كرة الزجاج", "كرة الزجاج تقفز أعلى من كرة المطاط", 2 , "المطاط", "الزجاج"));
        facts.add(new Facts("أوكي هي أول كلمة قيلت على سطح القمر", "استقبلت هي أول كلمة قيلت على سطح القمر", 1 , "أوكي", "استقبلت"));
        facts.add(new Facts("يخاف جميع سكان الأرض من الظلام", "يخاف نصف سكان الأرض من الظلام", 2 , "جميع", "نصف"));
        facts.add(new Facts("قلة النوم يمكن ان تجعلك تضحك بشكل غريب على أشياء عادية لا تستدعي الضحك", "كثرة النوم يمكن ان تجعلك تضحك بشكل غريب على أشياء عادية لا تستدعي الضحك", 1 , "قلة", "كثرة"));
        facts.add(new Facts("الديوك والدجاج لديهم القدرة على رؤية نور الفجر بعد البشر بـ ٤٥ دقيقة", "الديوك والدجاج لديهم القدرة على رؤية نور الفجر قبل البشر بـ ٤٥ دقيقة", 2 , "بعد", "قبل"));
        facts.add(new Facts("دائما هناك شخص بحياتك تشعر برغبتك الشديدة في لكمه مباشرة لسبب مقنع", "دائما هناك شخص بحياتك تشعر برغبتك الشديدة في لكمه مباشرة دون أي سبب مقنع", 2 , "لسبب", "دون أي سبب"));
        facts.add(new Facts("في اليابان تعتبر الأسنان السوداء لطيفة وجذابة", "في اليابان تعتبر الأسنان الملتوية لطيفة وجذابة", 2 , "السوداء", "الملتوية"));
        facts.add(new Facts("تتذكر الغربان وجوه البشر الذين يسيئون إليها", "تتذكر الكلاب وجوه البشر الذين يسيئون إليها", 1 , "الغربان", "الكلاب"));
        facts.add(new Facts("العاب الفيديو دائما تتسبب للطالب في الحصول على درجات اعلى في المدرسة", "العاب الفيديو قد تتسبب للطالب في الحصول على درجات اعلى في المدرسة", 2 , "دائما", "قد"));
        facts.add(new Facts("يستخدم الإنسان حوالي 200 عضلة ليخطو خطوة واحدة فقط", "يستخدم الإنسان حوالي 20 عضلة ليخطو خطوة واحدة فقط", 1 , "200", "20"));//110th question
      /*  facts.add(new Facts("", "", 2 , "", ""));
        facts.add(new Facts("", "", 1 , "", ""));
        facts.add(new Facts("", "", 2 , "", ""));
        facts.add(new Facts("", "", 1 , "", ""));
        facts.add(new Facts("", "", 2 , "", ""));
        facts.add(new Facts("", "", 1 , "", ""));
        facts.add(new Facts("", "", 2 , "", ""));
        facts.add(new Facts("", "", 1 , "", ""));
        facts.add(new Facts("", "", 2 , "", ""));
        facts.add(new Facts("", "", 1 , "", ""));
        facts.add(new Facts("", "", 2 , "", ""));
        facts.add(new Facts("", "", 1 , "", ""));
        facts.add(new Facts("", "", 2 , "", ""));
        facts.add(new Facts("", "", 1 , "", ""));
        facts.add(new Facts("", "", 2 , "", ""));
        facts.add(new Facts("", "", 1 , "", ""));
        facts.add(new Facts("", "", 2 , "", ""));
        facts.add(new Facts("", "", 1 , "", ""));
        facts.add(new Facts("", "", 2 , "", ""));
        facts.add(new Facts("", "", 1 , "", ""));
        facts.add(new Facts("", "", 2 , "", ""));
        facts.add(new Facts("", "", 1 , "", ""));
        facts.add(new Facts("", "", 2 , "", ""));
        facts.add(new Facts("", "", 1 , "", ""));
        facts.add(new Facts("", "", 2 , "", ""));
        facts.add(new Facts("", "", 1 , "", ""));
        facts.add(new Facts("", "", 2 , "", ""));
        facts.add(new Facts("", "", 1 , "", ""));
        facts.add(new Facts("", "", 2 , "", ""));
        facts.add(new Facts("", "", 1 , "", ""));
        facts.add(new Facts("", "", 2 , "", ""));
        facts.add(new Facts("", "", 1 , "", ""));
        facts.add(new Facts("", "", 2 , "", ""));
        facts.add(new Facts("", "", 1 , "", ""));
        facts.add(new Facts("", "", 2 , "", ""));
        facts.add(new Facts("", "", 1 , "", ""));
        facts.add(new Facts("", "", 2 , "", ""));
        facts.add(new Facts("", "", 2 , "", ""));
        facts.add(new Facts("", "", 1 , "", ""));
        facts.add(new Facts("", "", 2 , "", ""));
      //  facts.add(new Facts( "", "", 0 , "", ""));*/
    }

    void SetQuestion() {
        if (counter == facts.size()) {
            intent.putExtra("Score", score);
            startActivity(intent);
            return;
        }
        randNum = RandomNumber();

        Log.d("Setting question Number",""+randNum);

        setTextVieWords(text1,facts.get(randNum).fact1,facts.get(randNum).marked1,Color.rgb(0, 0, 255));
        setTextVieWords(text2,facts.get(randNum).fact2,facts.get(randNum).marked2,Color.rgb(0, 0, 255));

        int r=rand.nextInt(techniques.length);
        Log.w("Technique Number",""+r);

        YoYo.with(techniques[r])
                .duration(2000)
                .playOn(text1);
        YoYo.with(techniques[r])
                .duration(2000)
                .playOn(text2);

        scoreTxt.setText(""+(score));
        YoYo.with(Techniques.ZoomIn)
                .duration(700)
                .playOn(scoreTxt);

        img1.setEnabled(true);
        img2.setEnabled(true);
    }

    private void setTextVieWords(TextView view, String fulltext, String subtext, int color) {
            view.setText(fulltext, TextView.BufferType.SPANNABLE);
            Spannable str = (Spannable) view.getText();
            int i = fulltext.indexOf(subtext);
            str.setSpan(new ForegroundColorSpan(color), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public void AnswerQuestion(final int id) {
        img1.setEnabled(false);
        img2.setEnabled(false);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                int ans = facts.get(randNum).answer;

                counter++;
                answeredQues.add(randNum);

                intent.putExtra("Score", score);
                intent.putExtra("Loop",noloop);

                if (id == ans) {
                    mpC.start();
                    score++;
                    SetQuestion();
                }
                else
                {
                    startActivity(intent);
                }

            }
        }, 100);
    }

    int RandomNumber() {
        int num;
        unansweredQues.clear();
        for (int i = 0; i < facts.size(); i++) {
            if (!answeredQues.contains(i)) {
                unansweredQues.add(i);
            }
        }
        num = rand.nextInt(unansweredQues.size());
        return unansweredQues.get(num);
    }

    public void onPause() {
        super.onPause();
        DJ.iAmLeaving();
    }

    public void onResume(){
        super.onResume();
        DJ.iAmIn(this);
    }

}
