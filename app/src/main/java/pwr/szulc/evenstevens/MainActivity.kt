package pwr.szulc.evenstevens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pwr.szulc.evenstevens.data.DatabaseProvider.getDatabase
import pwr.szulc.evenstevens.data.dao.ExpenseDao
import pwr.szulc.evenstevens.data.dao.SplitEntryDao
import pwr.szulc.evenstevens.data.repositories.*
import pwr.szulc.evenstevens.data.viewmodels.*
import pwr.szulc.evenstevens.ui.screens.*
import pwr.szulc.evenstevens.ui.theme.EvenStevensTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = getDatabase(applicationContext)

        val groupRepository = GroupRepository(db.groupDao())
        val userRepository = UserRepository(db.userDao())
        val expenseRepository = ExpenseRepository(db.expenseDao(), db.splitEntryDao())
        val splitEntryRepository = SplitEntryRepository(db.splitEntryDao())
        val groupUserCrossRefRepository = GroupUserCrossRefRepository(db.groupUserCrossRefDao(), db.userDao(), db.expenseDao(), db.splitEntryDao())

        val groupViewModel = GroupViewModel(groupRepository)
        val userViewModel = UserViewModel(userRepository)
        val expenseViewModel = ExpenseViewModel(expenseRepository)
        val splitEntryViewModel = SplitEntryViewModel(splitEntryRepository)
        val groupUserCrossRefViewModel = GroupUserCrossRefViewModel(groupUserCrossRefRepository, db.groupUserCrossRefDao())

        setContent {
            var isDarkTheme by remember { mutableStateOf(true) }

            EvenStevensTheme(darkTheme = isDarkTheme) {
                AppNavHost(
                    groupViewModel = groupViewModel,
                    userViewModel = userViewModel,
                    expenseViewModel = expenseViewModel,
                    splitEntryViewModel = splitEntryViewModel,
                    groupUserCrossRefViewModel = groupUserCrossRefViewModel,
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { isDarkTheme = !isDarkTheme }
                )
            }
        }
    }
}

@Composable
fun AppNavHost(
    groupViewModel: GroupViewModel,
    userViewModel: UserViewModel,
    expenseViewModel: ExpenseViewModel,
    splitEntryViewModel: SplitEntryViewModel,
    groupUserCrossRefViewModel: GroupUserCrossRefViewModel,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                navController = navController,
                userViewModel = userViewModel,
                groupUserCrossRefViewModel = groupUserCrossRefViewModel,
                expenseViewModel = expenseViewModel,
                splitEntryViewModel = splitEntryViewModel,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
        composable("author_info") {
            AuthorInfoScreen(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
        composable("group_list") {
            GroupListScreen(
                navController = navController,
                viewModel = groupViewModel,
                groupUserCrossRefViewModel = groupUserCrossRefViewModel,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
        composable("add_group") {
            AddGroupScreen(
                navController = navController,
                groupViewModel = groupViewModel,
                userViewModel = userViewModel,
                groupUserCrossRefViewModel = groupUserCrossRefViewModel,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
        composable("edit_group/{groupId}") { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")?.toIntOrNull() ?: return@composable
            AddGroupScreen(
                navController = navController,
                groupViewModel = groupViewModel,
                userViewModel = userViewModel,
                groupUserCrossRefViewModel = groupUserCrossRefViewModel,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                editingGroupId = groupId
            )
        }
        composable("add_user") {
            AddUserScreen(
                navController = navController,
                userViewModel = userViewModel,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
        composable("group_screen/{groupId}") { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")?.toIntOrNull() ?: return@composable
            GroupScreen(
                groupId = groupId,
                navController = navController,
                userViewModel = userViewModel,
                groupViewModel = groupViewModel,
                expenseViewModel = expenseViewModel,
                groupUserCrossRefViewModel = groupUserCrossRefViewModel,
                splitEntryViewModel = splitEntryViewModel,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
        composable("add_expense/{groupId}") { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")?.toIntOrNull() ?: return@composable
            AddExpenseScreen(
                groupId = groupId,
                navController = navController,
                expenseViewModel = expenseViewModel,
                userViewModel = userViewModel,
                groupUserCrossRefViewModel = groupUserCrossRefViewModel,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
    }
}
