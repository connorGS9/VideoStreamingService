import { BrowserRouter, Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import UploadPage from './pages/UploadPage';
import WatchPage from './pages/WatchPage';


function App() {
    return (
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/upload" element={<UploadPage />} />
          <Route path="/watch/:id" element={<WatchPage />} />
        </Routes>
      </BrowserRouter>
    )
}

export default App
