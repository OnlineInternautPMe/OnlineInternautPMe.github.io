package org.onlineinternautpme.mywebpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import onlineinternautpmegithubio.composeapp.generated.resources.Res
import onlineinternautpmegithubio.composeapp.generated.resources.favicon
import org.jetbrains.compose.resources.painterResource
import kotlin.math.ceil
import kotlin.math.sqrt

// --- Models ---
// Note: We removed the 'route' string since we no longer need it for a NavHost
sealed class BottomNavItem(val title: String, val icon: ImageVector) {
    object About : BottomNavItem("Resumen", Icons.Default.Person)
    object Experience : BottomNavItem("Experiencia", Icons.Default.Work)
    object Education : BottomNavItem("Educación", Icons.Default.School)
    object SoftSkills : BottomNavItem("Soft Skills", Icons.Default.Star)
    object Interests : BottomNavItem("Intereses", Icons.Default.Favorite)
}

data class ExperienceItem(
    val title: String,
    val company: String,
    val dates: String,
    val skills: List<String>
)

// --- The Device Wrapper ---
@Composable
fun PhoneDeviceSimulator() {
    // A dark background to represent the desktop screen behind the phone
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF222222)),
        contentAlignment = Alignment.Center
    ) {
        // The Physical Phone Frame
        Column(
            modifier = Modifier
                //.width(400.dp) // Standard mobile width
                //.height(800.dp) // Standard mobile height
                //.clip(RoundedCornerShape(32.dp)) // Simulates the curved corners of a phone
                //.border(12.dp, Color.Black, RoundedCornerShape(20.dp)) // The physical bezel
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Top Android Status Bar
            FakeStatusBar()

            // Your actual Portfolio Application takes up the remaining space
            Box(modifier = Modifier.weight(1f)) {
                PortfolioApp()
            }

            // Bottom Android Navigation Bar
            FakeSystemNavBar()
        }
    }
}

// --- Fake Top Status Bar ---
@Composable
fun FakeStatusBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black) // Keeps the notch area dark
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Fake Time
        Text(
            text = "12:00",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        // Fake System Icons
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.SignalCellular4Bar,
                contentDescription = "Cellular",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
            Icon(
                imageVector = Icons.Default.Wifi,
                contentDescription = "Wifi",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
            Icon(
                imageVector = Icons.Default.BatteryFull,
                contentDescription = "Battery",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// --- Fake Bottom System Navigation Bar ---
@Composable
fun FakeSystemNavBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black) // Classic dark navigation bar
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Simulating the classic Android 3-button layout
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Icon(
            imageVector = Icons.Default.RadioButtonUnchecked, // Simulates the "Home" circle
            contentDescription = "Home",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Icon(
            imageVector = Icons.Default.CropSquare, // Simulates the "Recents" square
            contentDescription = "Recents",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

// --- Main App Component ---
@Composable
fun PortfolioApp() {
    // We store the currently selected tab in a state variable.
    // Defaulting to the 'About' section on launch.
    var selectedItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.About) }

    val items = listOf(
        BottomNavItem.About,
        BottomNavItem.Experience,
        BottomNavItem.Education,
        BottomNavItem.SoftSkills,
        BottomNavItem.Interests
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title, maxLines = 1) },
                        selected = selectedItem == item,
                        onClick = { selectedItem = item } // Update state on click
                    )
                }
            }
        }
    ) { innerPadding ->
        // We use a simple 'when' statement to swap the UI based on the current state.
        // No NavHost, no back stack, just pure UI recomposition.
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedItem) {
                BottomNavItem.About -> AboutMeScreen()
                BottomNavItem.Experience -> ExperienceScreen()
                BottomNavItem.Education -> EducationScreen()
                BottomNavItem.SoftSkills -> SoftSkillsScreen()
                BottomNavItem.Interests -> InterestsScreen()
            }
        }
    }
}

@Composable
fun DynamicGridTable(items: List<String>) {
    if (items.isEmpty()) return

    // Sort elements by length so the longest items fall to the bottom rows
    val sortedItems = items.sortedBy { it.length }

    val columns = ceil(sqrt(sortedItems.size.toDouble())).toInt()
    val rows = ceil(sortedItems.size.toDouble() / columns).toInt()

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        for (row in 0 until rows) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
            ) {
                val startIndex = row * columns
                val endIndex = minOf(startIndex + columns, sortedItems.size)
                val rowItems = sortedItems.subList(startIndex, endIndex)

                for (text in rowItems) {
                    Surface(
                        modifier = Modifier
                            // 1. Every item gets equal weight. If the last row has fewer items,
                            // they naturally stretch to share the full width.
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(2.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.small
                    ) {
                        // 2. Wrap the Text in a Box to easily center it vertically and horizontally
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = text,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- Screen 1: About Me ---
@Composable
fun AboutMeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Image(painterResource(Res.drawable.favicon), contentDescription = "Photo", modifier = Modifier.size(120.dp), contentScale = ContentScale.Fit)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Álvaro", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = "Radajczyk", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = "Sánchez", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Link, contentDescription = "LinkedIn", tint = Color.Gray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            Text(text = "linkedin.com/in/alvaro-radajczyk")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Web, contentDescription = "My Webpage", tint = Color.Gray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            Text(text = "onlineinternautpme.github.io")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Email, contentDescription = "Email", tint = Color.Gray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            Text(text = "alvaroradajczyk@protonmail.com")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Phone, contentDescription = "Phone Number", tint = Color.Gray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            Text(text = "+34 654 49 00 36")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Actualmente resido en Alcalá de Henares. Tengo 2 años de experiencia trabajando en equipo como desarrollador de Android, tanto en el frontend como en el backend. Además, recibo formación relacionada con la ciberseguridad, con la cual que he aprendido la importancia de la seguridad en los sistemas de información, tanto desde la parte técnica como en la de gobernanza y de concienciación",
            textAlign = TextAlign.Justify
        )
    }
}

// --- Screen 2: Experience ---
@Composable
fun ExperienceScreen() {
    val experiences = listOf(
        ExperienceItem("Desarrollador de Android", "Custos Mobile SL", "03/2024 - Actualidad",
            listOf("Android SDK", "Kotlin", "Jetpack Compose", "JUnit", "MVVM", "GitLab", "K8s", "WebRTC"))
    )

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Experiencia Laboral/Profesional", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Mi experiencia laboral", modifier = Modifier.padding(bottom = 16.dp))
        }
        items(experiences) { exp ->
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = exp.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "${exp.company} | ${exp.dates}")
                    Spacer(modifier = Modifier.height(8.dp))
                    // Text(text = "Skills Used:")
                    DynamicGridTable(exp.skills)
                }
            }
        }
    }
}

// --- Screen 3: Education ---
@Composable
fun EducationScreen() {
    val educations = listOf(
        ExperienceItem("Máster en Ciberseguridad", "Universidad de Alcalá de Henares", "2024 - Actualidad",
            listOf("GNU/Linux", "Burp Suite", "Splunk", "HackTheBox", "PortSwigger", "FLARE-VM", "CryptoHack", "JCrypTool")),
        ExperienceItem("Grado en Ingeniería Informática", "Universidad de Alcalá de Henares", "2020 - 2024",
            listOf("Azure", "Docker", "Git", "Hugging Face", "Angular", "Java", "C", "Python", "SQL", "Javascript"))
    )

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Educación Formativa", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Mi educación formativa", modifier = Modifier.padding(bottom = 16.dp))
        }
        items(educations) { edu ->
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = edu.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "${edu.company} | ${edu.dates}")
                    Spacer(modifier = Modifier.height(8.dp))
                    // Text(text = "Key Subjects:")
                    DynamicGridTable(edu.skills)
                }
            }
        }
    }
}

// --- Screen 4: Soft Skills ---
@Composable
fun SoftSkillsScreen() {
    val softSkills = listOf(
        "Leadership", "Communication", "Teamwork", "Problem Solving",
        "Adaptability", "Time Management", "Creativity", "Work Ethic"
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Soft Skills", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Personal attributes that enable me to interact effectively and harmoniously with other people.", modifier = Modifier.padding(vertical = 16.dp))
        DynamicGridTable(softSkills)
    }
}

// --- Screen 5: Interests ---
@Composable
fun InterestsScreen() {
    val interests = listOf(
        "Photography", "Traveling", "Reading", "Cooking",
        "Open Source", "Gaming", "Hiking", "Music"
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Interests", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("What I enjoy doing in my free time to stay creative and energized.", modifier = Modifier.padding(vertical = 16.dp))
        DynamicGridTable(interests)
    }
}

@Composable
fun App() {
    MaterialTheme {
        PhoneDeviceSimulator()
    }
}