import logo from '../assets/logo.png'

export default function Header() {
  return (
    <header className="w-full h-22 flex items-center justify-between px-[3.5%] bg-white">
      <Logo />
      <NavMenu />
      <UserButtons />
    </header>
  )
}

function Logo() {
  return (
    <img src={logo} alt="Fin 로고" className="h-7" />
  )
}

const navItems = ['서비스 소개', '금융상품 추천', '정보 커뮤니티', '마이페이지']

function NavMenu() {
  return (
    <ul className="flex gap-30 mx-16">
      {navItems.map((item, i) => (
        <li key={i} className="font-[Inter] font-medium text-[17px] text-[#515151] hover:text-gray-400 cursor-pointer whitespace-nowrap transition-colors">
          {item}
        </li>
      ))}
    </ul>
  )
}

function UserButtons() {
  return (
    <div
    className="flex items-center gap-3 font-inter text-[14.5px]">
      <LoginButton />
      <JoinButton />
    </div>
  )
}

function LoginButton() {
  return (
    <button className="text-[#515151] border border-gray-300 rounded-lg h-9 w-19.5 hover:border-[#03BFA5] hover:text-[#03BFA5] transition-colors whitespace-nowrap">
      로그인
    </button>
  )
}

function JoinButton() {
  return (
    <button className="text-white bg-[#03BFA5] rounded-lg h-9 w-19.5 hover:bg-[#02a892] transition-colors whitespace-nowrap">
      회원가입
    </button>
  )
}