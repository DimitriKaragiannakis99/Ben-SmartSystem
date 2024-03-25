import React from 'react';
import Profile from './components/Dashboard/Profile';
import HouseView from './components/Dashboard/HouseView';
import DashboardTabs from './components/Dashboard/DashboardTabs';
import RoomProvider from './components/Dashboard/RoomProvider';



const SmartHomeDashboard = () => {
  return (
  
    <RoomProvider>
    <div className='flex h-screen'>
        <div className='w-1/4'>
            <DashboardTabs />
        </div>
        <div className='w-0.5 bg-black'></div>
        <div className='w-1/2'>
            <HouseView />
        </div>
        <div className='w-0.5 bg-black'></div>
        <div className='w-1/4 items-center justify-center'>
            <Profile />
        </div>
    </div>
    </RoomProvider>
    
  );
};

export default SmartHomeDashboard;