import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Main from './pages/Main';
import Introduce from './pages/Introduce';
import Agreement from './pages/Agreement';
import AuthGuard from './routes/AuthGuard';
import Recommend from './pages/Recommend';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/introduce" element={<Introduce />} />
        <Route path="/recommend" element={<Recommend />} />
        
        <Route
          path="/"
          element={
            <AuthGuard>
              <Main />
            </AuthGuard>
          }
        />
        
        <Route path="/terms" element={<Agreement />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;