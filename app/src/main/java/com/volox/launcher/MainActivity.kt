package com.volox.launcher

import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Night = Color(0xFF07111F)
private val Panel = Color(0xFF10243A)
private val Blue = Color(0xFF258BFF)
private val Pale = Color(0xFFEAF4FF)

data class LaunchApp(
    val label: String,
    val packageName: String,
    val resolveInfo: ResolveInfo
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { VoloXLauncher() }
    }
}

@Composable
fun VoloXLauncher() {
    val context = LocalContext.current
    var showDrawer by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }
    var apps by remember { mutableStateOf(emptyList<LaunchApp>()) }

    fun loadApps() {
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        apps = context.packageManager.queryIntentActivities(intent, 0)
            .map {
                LaunchApp(
                    label = it.loadLabel(context.packageManager).toString(),
                    packageName = it.activityInfo.packageName,
                    resolveInfo = it
                )
            }
            .distinctBy { it.packageName }
            .sortedBy { it.label.lowercase(Locale.getDefault()) }
    }

    LaunchedEffect(Unit) { loadApps() }

    val filteredApps = apps.filter { it.label.contains(search, ignoreCase = true) }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Blue,
            background = Night,
            surface = Panel,
            onBackground = Pale,
            onSurface = Pale
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0A2440), Night, Color(0xFF030811))
                    )
                )
                .padding(horizontal = 22.dp, vertical = 24.dp)
        ) {
            if (!showDrawer) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("VOLOX", color = Blue, fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 4.sp)
                                Text("OS", color = Pale, fontSize = 12.sp, letterSpacing = 3.sp)
                            }
                            IconButton(onClick = {
                                context.startActivity(Intent(Settings.ACTION_SETTINGS))
                            }) {
                                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Pale)
                            }
                        }

                        Spacer(Modifier.height(34.dp))
                        Text(
                            SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(Date()),
                            color = Pale.copy(alpha = 0.75f),
                            fontSize = 15.sp
                        )
                        Text(
                            SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                            color = Pale,
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Light
                        )

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Panel.copy(alpha = 0.88f)),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(18.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier.size(44.dp).clip(CircleShape).background(Blue.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("V", color = Blue, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                }
                                Column(Modifier.weight(1f)) {
                                    Text("VoloX AI", color = Pale, fontWeight = FontWeight.SemiBold)
                                    Text("Your assistant, one tap away", color = Pale.copy(alpha = 0.65f), fontSize = 12.sp)
                                }
                                Text("OPEN", color = Blue, fontSize = 12.sp, fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable {
                                        // Placeholder until the VoloX AI app or web destination is connected.
                                    }.padding(8.dp)
                                )
                            }
                        }

                        Text("QUICK ACCESS", color = Pale.copy(alpha = 0.55f), fontSize = 11.sp, letterSpacing = 2.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            QuickTile("Wi-Fi", Icons.Default.Wifi, Modifier.weight(1f)) {
                                context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                            }
                            QuickTile("Settings", Icons.Default.Settings, Modifier.weight(1f)) {
                                context.startActivity(Intent(Settings.ACTION_SETTINGS))
                            }
                            QuickTile("All apps", Icons.Default.Apps, Modifier.weight(1f)) {
                                search = ""
                                showDrawer = true
                            }
                        }
                    }

                    Button(
                        onClick = { search = ""; showDrawer = true },
                        modifier = Modifier.fillMaxWidth().height(58.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Blue)
                    ) {
                        Icon(Icons.Default.Apps, contentDescription = null)
                        Spacer(Modifier.width(10.dp))
                        Text("Open app drawer", fontWeight = FontWeight.SemiBold)
                    }
                }
            } else {
                Column(Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("VOLOX OS", color = Blue, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                            Text("${apps.size} apps", color = Pale.copy(alpha = 0.65f), fontSize = 12.sp)
                        }
                        TextButton(onClick = { showDrawer = false }) { Text("HOME", color = Blue) }
                    }
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = search,
                        onValueChange = { search = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("Search apps") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Pale,
                            unfocusedTextColor = Pale,
                            focusedBorderColor = Blue,
                            unfocusedBorderColor = Pale.copy(alpha = 0.25f),
                            focusedLeadingIconColor = Blue,
                            unfocusedLeadingIconColor = Pale.copy(alpha = 0.6f)
                        )
                    )
                    Spacer(Modifier.height(12.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        contentPadding = PaddingValues(bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredApps, key = { it.packageName }) { app ->
                            Column(
                                modifier = Modifier.fillMaxWidth().clickable {
                                    val launch = context.packageManager.getLaunchIntentForPackage(app.packageName)
                                    if (launch != null) context.startActivity(launch)
                                }.padding(vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier.size(54.dp).clip(RoundedCornerShape(18.dp))
                                        .background(Brush.linearGradient(listOf(Blue, Color(0xFF164A80)))),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        app.label.take(1).uppercase(),
                                        color = Color.White,
                                        fontSize = 23.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(Modifier.height(7.dp))
                                Text(
                                    app.label,
                                    color = Pale,
                                    fontSize = 11.sp,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickTile(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.height(94.dp).clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Panel.copy(alpha = 0.9f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = Blue, modifier = Modifier.size(25.dp))
            Spacer(Modifier.height(8.dp))
            Text(label, color = Pale, fontSize = 11.sp)
        }
    }
}
