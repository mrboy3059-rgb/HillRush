package com.maxfun.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.maxfun.app.ui.theme.MaxFunTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaxFunTheme { MaxFunApp() } }
    }
}

data class Story(val name: String, val permanent: Boolean = false)
data class VideoPost(val name: String, val caption: String, val views: String)
data class ChatItem(val name: String, val text: String, val online: Boolean)

@Composable
fun MaxFunApp() {
    val nav = rememberNavController()
    val tabs = listOf("home","videos","chat","live","profile")
    val current = nav.currentBackStackEntryAsState().value?.destination?.route ?: "home"

    Scaffold(
        containerColor = Color(0xFF080A12),
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF11131D)) {
                val items = listOf(
                    Triple("home","Home",Icons.Default.Home),
                    Triple("videos","Videos",Icons.Default.PlayCircle),
                    Triple("chat","Chat",Icons.Default.Chat),
                    Triple("live","Live",Icons.Default.Videocam),
                    Triple("profile","Profile",Icons.Default.Person)
                )
                items.forEach { (route,label,icon) ->
                    NavigationBarItem(
                        selected = current == route,
                        onClick = { nav.navigate(route) { popUpTo("home"); launchSingleTop = true } },
                        icon = { Icon(icon, null) },
                        label = { Text(label, fontSize = 10.sp) }
                    )
                }
            }
        }
    ) { pad ->
        NavHost(nav, startDestination = "home", modifier = Modifier.padding(pad)) {
            composable("home") { HomeScreen(nav) }
            composable("videos") { VideosScreen(nav) }
            composable("chat") { ChatScreen(nav) }
            composable("live") { LiveScreen(nav) }
            composable("profile") { ProfileScreen(nav) }
            composable("settings") { SettingsScreen(nav) }
            composable("add") { AddScreen(nav) }
            composable("groups") { GroupsScreen(nav) }
            composable("notifications") { NotificationsScreen(nav) }
            composable("search") { SearchScreen(nav) }
        }
    }
}

@Composable
fun TopBar(title: String, nav: NavHostController, actions: @Composable RowScope.() -> Unit = {}) {
    Row(
        Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        actions()
    }
}

@Composable
fun HomeScreen(nav: NavHostController) {
    val stories = listOf(Story("Your story"), Story("Aziza"), Story("Ali"), Story("Madina"), Story("Bek"), Story("MaxFun"))
    val posts = listOf(
        VideoPost("Aziza", "Bugungi yangi video 🔥", "12.4K views"),
        VideoPost("Ali", "Weekend vibes ✨", "8.1K views"),
        VideoPost("Madina", "MaxFun story time", "21K views")
    )
    LazyColumn(Modifier.fillMaxSize().background(Color(0xFF080A12))) {
        item {
            TopBar("MaxFun", nav) {
                IconButton({ nav.navigate("search") }) { Icon(Icons.Default.Search, "Search", tint = Color.White) }
                IconButton({ nav.navigate("notifications") }) { Icon(Icons.Default.Notifications, "Notifications", tint = Color.White) }
                IconButton({ nav.navigate("settings") }) { Icon(Icons.Default.Settings, "Settings", tint = Color.White) }
            }
        }
        item {
            Text("Stories", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal=16.dp))
            LazyRow(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(stories) { s ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            Modifier.size(64.dp).clip(CircleShape).background(
                                Brush.linearGradient(listOf(Color(0xFF7C3AED), Color(0xFFEC4899), Color(0xFF06B6D4)))
                            ), contentAlignment = Alignment.Center
                        ) { Text(if(s.permanent) "★" else "+", color=Color.White, fontSize=22.sp) }
                        Text(s.name, color=Color.LightGray, fontSize=11.sp)
                    }
                }
            }
        }
        item {
            Row(Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                QuickAction("＋", "Add", Color(0xFF7C3AED)) { nav.navigate("add") }
                QuickAction("▣", "Video", Color(0xFFEC4899)) { nav.navigate("videos") }
                QuickAction("●", "Note", Color(0xFF06B6D4)) { nav.navigate("add") }
                QuickAction("✦", "Live", Color(0xFFF43F5E)) { nav.navigate("live") }
            }
        }
        item { Text("For You", color=Color.White, fontSize=20.sp, fontWeight=FontWeight.Bold, modifier=Modifier.padding(16.dp)) }
        items(posts) { p -> VideoCard(p) }
    }
}

@Composable
fun QuickAction(icon:String, label:String, color:Color, onClick:()->Unit) {
    Column(Modifier.weight(1f).clickable(onClick=onClick), horizontalAlignment=Alignment.CenterHorizontally) {
        Box(Modifier.size(48.dp).clip(CircleShape).background(color), contentAlignment=Alignment.Center) {
            Text(icon, color=Color.White, fontSize=22.sp)
        }
        Text(label, color=Color.LightGray, fontSize=11.sp)
    }
}

@Composable
fun VideoCard(p: VideoPost) {
    Column(Modifier.padding(horizontal=16.dp, vertical=8.dp)) {
        Row(verticalAlignment=Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Color.Magenta,Color.Cyan))))
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(p.name, color=Color.White, fontWeight=FontWeight.Bold)
                Text("online", color=Color(0xFF4ADE80), fontSize=11.sp)
            }
            Icon(Icons.Default.MoreVert, null, tint=Color.LightGray)
        }
        Spacer(Modifier.height(8.dp))
        Box(Modifier.fillMaxWidth().height(270.dp).clip(RoundedCornerShape(18.dp)).background(
            Brush.linearGradient(listOf(Color(0xFF312E81),Color(0xFFBE185D),Color(0xFF0E7490)))
        ), contentAlignment=Alignment.Center) {
            Icon(Icons.Default.PlayCircle, null, tint=Color.White, modifier=Modifier.size(70.dp))
        }
        Text(p.caption, color=Color.White, fontWeight=FontWeight.SemiBold, modifier=Modifier.padding(top=8.dp))
        Row(Modifier.fillMaxWidth().padding(vertical=6.dp), horizontalArrangement=Arrangement.spacedBy(18.dp)) {
            Text("♡  2.4K", color=Color.LightGray); Text("💬  184", color=Color.LightGray); Text("↗  Share", color=Color.LightGray); Text(p.views, color=Color.Gray)
        }
    }
}

@Composable
fun VideosScreen(nav: NavHostController) {
    Column(Modifier.fillMaxSize().background(Color(0xFF080A12))) {
        TopBar("Videos", nav) { IconButton({nav.navigate("search")}) { Icon(Icons.Default.Search,null,tint=Color.White)} }
        LazyColumn {
            item { Text("Trending videos", color=Color.White, fontSize=20.sp, fontWeight=FontWeight.Bold, modifier=Modifier.padding(16.dp)) }
            items((1..8).map { VideoPost("Creator $it","MaxFun video #$it","${it*3}.2K views") }) { VideoCard(it) }
        }
    }
}

@Composable
fun ChatScreen(nav: NavHostController) {
    val chats = listOf(ChatItem("Aziza","Salom! Qalaysan?",true),ChatItem("Ali","Video yubordim",false),ChatItem("Madina","Bugun live qilamizmi?",true),ChatItem("Bek","👍",false))
    Column(Modifier.fillMaxSize().background(Color(0xFF080A12))) {
        TopBar("Chat", nav) { IconButton({nav.navigate("groups")}) { Icon(Icons.Default.Group,null,tint=Color.White)} }
        LazyColumn(Modifier.fillMaxSize()) {
            item { Button(onClick={}, modifier=Modifier.fillMaxWidth().padding(16.dp), colors=ButtonDefaults.buttonColors(containerColor=Color(0xFF7C3AED))) { Text("＋ New chat") } }
            items(chats) { c ->
                Row(Modifier.fillMaxWidth().clickable{}.padding(16.dp), verticalAlignment=Alignment.CenterVertically) {
                    Box(Modifier.size(52.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Color(0xFF7C3AED),Color(0xFF06B6D4)))))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) { Text(c.name,color=Color.White,fontWeight=FontWeight.Bold); Text(c.text,color=Color.Gray) }
                    if(c.online) Box(Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF22C55E)))
                }
            }
        }
    }
}

@Composable
fun GroupsScreen(nav: NavHostController) {
    Column(Modifier.fillMaxSize().background(Color(0xFF080A12))) {
        TopBar("Groups", nav)
        Button(onClick={},modifier=Modifier.padding(16.dp).fillMaxWidth()) { Text("＋ Create group") }
        listOf("MaxFun Creators","Friends","Study Group","Live Team").forEach {
            Row(Modifier.fillMaxWidth().padding(16.dp),verticalAlignment=Alignment.CenterVertically) {
                Box(Modifier.size(50.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFF312E81)),contentAlignment=Alignment.Center){Text("👥")}
                Spacer(Modifier.width(12.dp)); Text(it,color=Color.White,fontSize=17.sp)
            }
        }
    }
}

@Composable
fun LiveScreen(nav: NavHostController) {
    Column(Modifier.fillMaxSize().background(Color(0xFF080A12))) {
        TopBar("Live", nav)
        Box(Modifier.fillMaxWidth().height(280.dp).padding(16.dp).clip(RoundedCornerShape(24.dp)).background(
            Brush.linearGradient(listOf(Color(0xFF450A0A),Color(0xFF7F1D1D),Color(0xFF111827)))
        ),contentAlignment=Alignment.Center) {
            Column(horizontalAlignment=Alignment.CenterHorizontally) {
                Icon(Icons.Default.Videocam,null,tint=Color.White,modifier=Modifier.size(70.dp))
                Text("LIVE STREAM",color=Color.White,fontWeight=FontWeight.Bold,fontSize=24.sp)
                Text("Streaming service can be connected in v2",color=Color.LightGray)
            }
        }
        Button(onClick={},modifier=Modifier.fillMaxWidth().padding(16.dp),colors=ButtonDefaults.buttonColors(containerColor=Color(0xFFF43F5E))) { Text("🔴 Start Live") }
        Text("Live now",color=Color.White,fontSize=20.sp,fontWeight=FontWeight.Bold,modifier=Modifier.padding(16.dp))
        listOf("Aziza • 1.2K watching","MaxFun Official • 642 watching","Ali • 188 watching").forEach {
            Text(it,color=Color.LightGray,modifier=Modifier.padding(horizontal=16.dp,vertical=10.dp))
        }
    }
}

@Composable
fun ProfileScreen(nav: NavHostController) {
    Column(Modifier.fillMaxSize().background(Color(0xFF080A12))) {
        TopBar("Profile", nav) { IconButton({nav.navigate("settings")}) { Icon(Icons.Default.Settings,null,tint=Color.White)} }
        Column(horizontalAlignment=Alignment.CenterHorizontally,modifier=Modifier.fillMaxWidth()) {
            Box(Modifier.size(100.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Color(0xFF7C3AED),Color(0xFFEC4899),Color(0xFF06B6D4))),),contentAlignment=Alignment.Center) {
                Text("M",color=Color.White,fontSize=42.sp,fontWeight=FontWeight.Bold)
            }
            Text("MaxFun User",color=Color.White,fontSize=22.sp,fontWeight=FontWeight.Bold)
            Text("@maxfun_user",color=Color.Gray)
            Text("Bio: Create • Share • Connect ✨",color=Color.LightGray,modifier=Modifier.padding(10.dp))
            Row(Modifier.fillMaxWidth().padding(12.dp),horizontalArrangement=Arrangement.SpaceEvenly) {
                Stat("Videos","24"); Stat("Followers","1.8K"); Stat("Following","312"); Stat("Streams","16")
            }
            Row(Modifier.fillMaxWidth().padding(8.dp),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick={}) { Text("Edit profile") }
                Button(onClick={}) { Text("Share profile") }
            }
        }
        TabRow(selectedTabIndex=0,containerColor=Color.Transparent) {
            listOf("Videos","Stories","Streams").forEachIndexed { i,s -> Tab(selected=i==0,onClick={},text={Text(s)}) }
        }
        LazyRow(Modifier.padding(12.dp),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            items(9) { Box(Modifier.size(112.dp).clip(RoundedCornerShape(12.dp)).background(
                Brush.linearGradient(listOf(Color(0xFF312E81),Color(0xFF0E7490)))
            ),contentAlignment=Alignment.Center){Icon(Icons.Default.PlayCircle,null,tint=Color.White,modifier=Modifier.size(35.dp))} }
        }
    }
}

@Composable fun Stat(label:String,value:String) {
    Column(horizontalAlignment=Alignment.CenterHorizontally){Text(value,color=Color.White,fontWeight=FontWeight.Bold,fontSize=18.sp);Text(label,color=Color.Gray,fontSize=11.sp)}
}

@Composable
fun AddScreen(nav: NavHostController) {
    var selected by remember { mutableStateOf("Video") }
    var permanent by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().background(Color(0xFF080A12))) {
        TopBar("Create", nav)
        Row(Modifier.padding(16.dp),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            listOf("Video","Story","Note").forEach { t ->
                FilterChip(selected=t==selected,onClick={selected=t},label={Text(t)})
            }
        }
        Box(Modifier.fillMaxWidth().height(260.dp).padding(16.dp).clip(RoundedCornerShape(24.dp)).background(
            Brush.linearGradient(listOf(Color(0xFF312E81),Color(0xFFBE185D),Color(0xFF0E7490)))
        ),contentAlignment=Alignment.Center) {
            Icon(Icons.Default.AddAPhoto,null,tint=Color.White,modifier=Modifier.size(64.dp))
        }
        Text(if(selected=="Story") "Story lifetime" else "Content details",color=Color.White,fontSize=19.sp,fontWeight=FontWeight.Bold,modifier=Modifier.padding(16.dp))
        if(selected=="Story") {
            Row(Modifier.fillMaxWidth().padding(horizontal=16.dp),verticalAlignment=Alignment.CenterVertically) {
                Text(if(permanent) "Permanent story" else "24-hour story",color=Color.White,modifier=Modifier.weight(1f))
                Switch(permanent,{permanent=it})
            }
        }
        OutlinedTextField(value="",onValueChange={},label={Text(if(selected=="Note")"Write a note" else "Caption")},modifier=Modifier.fillMaxWidth().padding(16.dp))
        Button(onClick={},modifier=Modifier.fillMaxWidth().padding(16.dp),colors=ButtonDefaults.buttonColors(containerColor=Color(0xFF7C3AED))) {
            Text("Publish $selected")
        }
    }
}

@Composable fun SettingsScreen(nav: NavHostController) {
    val rows=listOf("Account","Notifications","Privacy & Safety","Appearance","Language","Blocked users","Help & Support")
    Column(Modifier.fillMaxSize().background(Color(0xFF080A12))) {
        TopBar("Settings",nav)
        rows.forEach { Row(Modifier.fillMaxWidth().clickable{}.padding(18.dp),verticalAlignment=Alignment.CenterVertically) {
            Icon(Icons.Default.Settings,null,tint=Color.LightGray); Spacer(Modifier.width(16.dp)); Text(it,color=Color.White,modifier=Modifier.weight(1f)); Icon(Icons.Default.ChevronRight,null,tint=Color.Gray)
        }}
        Button(onClick={},modifier=Modifier.padding(16.dp).fillMaxWidth(),colors=ButtonDefaults.buttonColors(containerColor=Color(0xFFB91C1C))){Text("Log out")}
    }
}

@Composable fun NotificationsScreen(nav: NavHostController) {
    Column(Modifier.fillMaxSize().background(Color(0xFF080A12))) {
        TopBar("Notifications",nav)
        listOf("Aziza liked your video","Ali started a live stream","Madina followed you","Your story got 24 views").forEach {
            Row(Modifier.padding(16.dp),verticalAlignment=Alignment.CenterVertically){Icon(Icons.Default.Notifications,null,tint=Color(0xFFEC4899));Spacer(Modifier.width(12.dp));Text(it,color=Color.White)}
        }
    }
}

@Composable fun SearchScreen(nav: NavHostController) {
    var q by remember{mutableStateOf("")}
    Column(Modifier.fillMaxSize().background(Color(0xFF080A12))) {
        TopBar("Search",nav)
        OutlinedTextField(q,{q=it},label={Text("Search people, videos, groups")},modifier=Modifier.fillMaxWidth().padding(16.dp))
        Text("Popular",color=Color.White,fontWeight=FontWeight.Bold,modifier=Modifier.padding(16.dp))
        listOf("#maxfun","#live","#music","#friends").forEach{ Text(it,color=Color(0xFF38BDF8),modifier=Modifier.padding(horizontal=20.dp,vertical=8.dp)) }
    }
}
